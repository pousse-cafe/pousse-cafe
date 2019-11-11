package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApmTransactionLabels;
import poussecafe.apm.ApmTransactionResults;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageConsumptionReport;
import poussecafe.environment.MessageListener;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.stream.Collectors.toList;

public class MessageConsumption {

    public static class Builder {

        private MessageConsumption consumption = new MessageConsumption();

        public Builder consumptionId(String consumptionId) {
            consumption.consumptionId = consumptionId;
            return this;
        }

        public Builder logger(Logger logger) {
            consumption.logger = logger;
            return this;
        }

        public Builder listenersPartition(ListenersSetPartition listenersPartition) {
            consumption.listenersPartition = listenersPartition;
            return this;
        }

        public Builder failFast(boolean failFast) {
            consumption.failFast = failFast;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            consumption.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            consumption.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder message(OriginalAndMarshaledMessage message) {
            consumption.message = message;
            return this;
        }

        public MessageConsumption build() {
            Objects.requireNonNull(consumption.consumptionId);
            Objects.requireNonNull(consumption.listenersPartition);
            Objects.requireNonNull(consumption.messageConsumptionHandler);
            Objects.requireNonNull(consumption.applicationPerformanceMonitoring);
            Objects.requireNonNull(consumption.logger);
            Objects.requireNonNull(consumption.message);
            return consumption;
        }
    }

    private MessageConsumption() {

    }

    private String consumptionId;

    private OriginalAndMarshaledMessage message;

    public void execute() {
        logger.debug("Handling received message {}", message.original());
        List<MessageListener> listeners = listenersPartition.partitionListenersSet().messageListenersOf(message.original().getClass()).stream()
                .sorted()
                .collect(toList());
        if(!listeners.isEmpty()) {
            logger.debug("  Found {} listeners", listeners.size());
            List<MessageListener> toRetryInitially = consumeMessageOrRetryListeners(message, consumptionId, listeners);
            if(!toRetryInitially.isEmpty()) {
                retryConsumption(message, consumptionId, toRetryInitially);
            }
        }
        logger.debug("Message {} handled (consumption ID {})", message.original(), consumptionId);
    }

    private ListenersSetPartition listenersPartition;

    private List<MessageListener> consumeMessageOrRetryListeners(OriginalAndMarshaledMessage message,
            String consumptionId,
            List<MessageListener> listeners) {
        List<MessageListener> toRetry = new ArrayList<>();
        for (MessageListener listener : listeners) {
            if(consumeMessageOrRetry(consumptionId, message, listener)) {
                toRetry.add(listener);
            }
        }
        return toRetry;
    }

    private void retryConsumption(OriginalAndMarshaledMessage message,
            String consumptionId,
            List<MessageListener> toRetryInitially) {
        int retry = 1;
        List<MessageListener> toRetry = toRetryInitially;
        while(!toRetry.isEmpty() && retry <= MAX_RETRIES) {
            logger.debug("    Try #{} for {} listeners", retry, toRetry.size());
            toRetry = consumeMessageOrRetryListeners(message, consumptionId, toRetry);
            ++retry;
        }
    }

    private static final int MAX_RETRIES = 10;

    protected Logger logger;

    private boolean consumeMessageOrRetry(String consumptionId,
            OriginalAndMarshaledMessage receivedMessage,
            MessageListener listener) {

        MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                .consumptionId(consumptionId)
                .listener(listener)
                .messageConsumptionHandler(messageConsumptionHandler)
                .receivedMessage(receivedMessage)
                .failFast(failFast)
                .build();

        MessageConsumptionReport report = executeInApmTransaction(executor);
        return report.mustRetry();
    }

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private MessageConsumptionReport executeInApmTransaction(MessageListenerExecutor executor) {
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
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            apmTransaction.captureException(e);
            return new MessageConsumptionReport.Builder()
                    .failure(e)
                    .build();
        } finally {
            apmTransaction.end();
        }
    }

    private void configureApmTransaction(MessageListenerExecutor executor, ApmTransaction apmTransaction) {
        MessageConsumptionReport report = executor.messageConsumptionReport();
        if(report.isSuccess()) {
            apmTransaction.setResult(ApmTransactionResults.SUCCESS);
        } else if(report.mustRetry()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
        } else if(report.isSkipped()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            if(!report.failures().isEmpty()) {
                apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, report.failures().get(0).getClass().getSimpleName());
            }
        } else if(report.isFailed()) {
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            apmTransaction.captureException(report.failures().get(0));
        } else {
            throw new IllegalArgumentException("Unsupported consumption report");
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
