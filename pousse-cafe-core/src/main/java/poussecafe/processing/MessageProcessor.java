package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.ConsumptionIdGenerator;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.util.MethodInvokerException;

import static java.util.stream.Collectors.toList;

class MessageProcessor {

    public static class Builder {

        private MessageProcessor processor = new MessageProcessor();

        public Builder id(String id) {
            processor.consumptionIdGenerator = new ConsumptionIdGenerator(id);
            return this;
        }

        public Builder listenersPartition(ListenersSetPartition listenersPartition) {
            processor.listenersPartition = listenersPartition;
            return this;
        }

        public Builder failFast(boolean failFast) {
            processor.failFast = failFast;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            processor.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            processor.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public MessageProcessor build() {
            Objects.requireNonNull(processor.consumptionIdGenerator);
            Objects.requireNonNull(processor.listenersPartition);
            Objects.requireNonNull(processor.messageConsumptionHandler);
            Objects.requireNonNull(processor.applicationPerformanceMonitoring);
            processor.logger = LoggerFactory.getLogger(MessageProcessor.class.getName() + "_" + processor.consumptionIdGenerator.prefix());
            return processor;
        }
    }

    private MessageProcessor() {

    }

    private boolean failFast;

    private MessageConsumptionHandler messageConsumptionHandler;

    public void processMessage(OriginalAndMarshaledMessage message) {
        String consumptionId = consumptionIdGenerator.next();
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

    private ConsumptionIdGenerator consumptionIdGenerator;

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
        String messageClassName = receivedMessage.original().getClass().getName();
        logger.debug("    {} consumes {}", listener, messageClassName);

        String transactionName = listener.shortId();
        ApmTransaction apmTransaction = applicationPerformanceMonitoring.startTransaction(transactionName);
        try {
            listener.consumer().accept(receivedMessage.original());
            logger.debug("      Success of {} with {}", listener, messageClassName);
            messageConsumptionHandler.handleSuccess(consumptionId, receivedMessage, listener);
            apmTransaction.setResult(APM_TRANSACTION_SUCCESS);
            return false;
        } catch (SameOperationException e) {
            apmTransaction.captureException(e);
            logger.warn("       Ignoring probable dubbed message consumption", e);
            apmTransaction.setResult(APM_TRANSACTION_SKIP);
            return false;
        } catch (OptimisticLockingException e) {
            if(!handleOptimisticLockingException(consumptionId, receivedMessage, listener, e)) {
                apmTransaction.captureException(e);
                apmTransaction.setResult(APM_TRANSACTION_FAILURE);
                return false;
            } else {
                apmTransaction.setResult(APM_TRANSACTION_SKIP);
                return true;
            }
        } catch (MethodInvokerException e) {
            apmTransaction.captureException(e.getCause());
            apmTransaction.setResult(APM_TRANSACTION_FAILURE);
            return handleConsumptionError(consumptionId, receivedMessage, listener, messageClassName, e);
        } catch (Exception e) {
            apmTransaction.captureException(e);
            apmTransaction.setResult(APM_TRANSACTION_FAILURE);
            return handleConsumptionError(consumptionId, receivedMessage, listener, messageClassName, e);
        } finally {
            apmTransaction.end();
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private static final String APM_TRANSACTION_SUCCESS = "success";

    private static final String APM_TRANSACTION_SKIP = "skip";

    private boolean handleOptimisticLockingException(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, OptimisticLockingException e) {
        if(messageConsumptionHandler.retryOnOptimisticLockingException(receivedMessage)) {
            logger.warn("       Optimistic locking failure detected, will retry", e);
            return true;
        } else {
            messageConsumptionHandler.handleFailure(consumptionId, receivedMessage, listener, e);
            return false;
        }
    }

    private static final String APM_TRANSACTION_FAILURE = "failure";

    private boolean handleConsumptionError(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, String messageClassName, Exception e) {
        if(failFast) {
            logger.error("      Failing fast on exception from listener {}", listener, e);
            throw new FailFastException("Failing fast on exception from listener {}", e);
        } else {
            logger.error("      Failure of {} with {}", listener, messageClassName, e);
            messageConsumptionHandler.handleFailure(consumptionId, receivedMessage, listener, e);
            return false;
        }
    }
}
