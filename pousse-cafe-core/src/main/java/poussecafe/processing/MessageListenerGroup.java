package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApmTransactionLabels;
import poussecafe.apm.ApmTransactionResults;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.util.MethodInvokerException;

public class MessageListenerGroup {

    public static class Builder {

        private MessageListenerGroup group = new MessageListenerGroup();

        public Builder consumptionId(String consumptionId) {
            group.consumptionId = consumptionId;
            return this;
        }

        public Builder message(OriginalAndMarshaledMessage message) {
            group.message = message;
            return this;
        }

        public Builder listeners(List<MessageListener> listeners) {
            group.listeners = listeners;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            group.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            group.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder failFast(boolean failFast) {
            group.failFast = failFast;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateRootClass(Optional<Class> aggregateRootClass) {
            group.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public MessageListenerGroup build() {
            Objects.requireNonNull(group.consumptionId);
            Objects.requireNonNull(group.message);
            Objects.requireNonNull(group.listeners);
            Objects.requireNonNull(group.messageConsumptionHandler);
            Objects.requireNonNull(group.applicationPerformanceMonitoring);
            Objects.requireNonNull(group.aggregateRootClass);
            group.listeners.sort(null);
            return group;
        }
    }

    private String consumptionId;

    private OriginalAndMarshaledMessage message;

    private List<MessageListener> listeners;

    public List<MessageListenerConsumptionReport> consumeMessageOrRetry(MessageListenerGroupConsumptionState consumptionState) {
        List<MessageListenerConsumptionReport> reports = new ArrayList<>();
        for(MessageListener listener : listeners) {
            MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                    .consumptionId(consumptionId)
                    .listener(listener)
                    .messageConsumptionHandler(messageConsumptionHandler)
                    .consumptionState(consumptionState)
                    .failFast(failFast)
                    .build();
            reports.add(executeInApmTransaction(executor));
        }
        return reports;
    }

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private MessageListenerConsumptionReport executeInApmTransaction(MessageListenerExecutor executor) {
        String transactionName = executor.listener().shortId();
        ApmTransaction apmTransaction = applicationPerformanceMonitoring.startTransaction(transactionName);
        try {
            executor.executeListener();
            configureApmTransaction(executor, apmTransaction);
            return executor.messageConsumptionReport();
        } catch (FailFastException e) {
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            apmTransaction.captureException(e);
            throw e;
        } catch (Exception e) {
            logger.error("Listener failed", e);
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            captureException(apmTransaction, e);
            return new MessageListenerConsumptionReport.Builder(executor.listener().shortId())
                    .failure(e)
                    .build();
        } finally {
            apmTransaction.end();
        }
    }

    private void captureException(ApmTransaction apmTransaction, Throwable e) {
        if(e instanceof MethodInvokerException) {
            apmTransaction.captureException(e.getCause());
        } else {
            apmTransaction.captureException(e);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void configureApmTransaction(MessageListenerExecutor executor, ApmTransaction apmTransaction) {
        MessageListenerConsumptionReport report = executor.messageConsumptionReport();
        if(report.isSuccess()) {
            apmTransaction.setResult(ApmTransactionResults.SUCCESS);
        } else if(report.isFailed()) {
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            if(report.failure() != null) {
                captureException(apmTransaction, report.failure());
            } else if(!report.failures().isEmpty()) {
                Iterator<Throwable> exceptionsIterator = report.failures().values().iterator();
                Throwable oneException = exceptionsIterator.next();
                captureException(apmTransaction, oneException);
                if(exceptionsIterator.hasNext()) {
                    logger.warn("Some exceptions where not captured by APM");
                }
            }
        } else if(report.mustRetry()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, "retry_later");
        } else if(report.isSkipped()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, "already_executed");
        } else {
            throw new IllegalArgumentException("Unsupported consumption report");
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    @SuppressWarnings("rawtypes")
    public Optional<Class> aggregateRootClass() {
        return aggregateRootClass;
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateRootClass = Optional.empty();

    public List<MessageListener> listeners() {
        return Collections.unmodifiableList(listeners);
    }
}
