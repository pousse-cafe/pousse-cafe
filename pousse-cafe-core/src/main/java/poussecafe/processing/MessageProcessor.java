package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApmTransactionLabels;
import poussecafe.apm.ApmTransactionResults;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.runtime.ConsumptionIdGenerator;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.MessageListenerExecutionStatus;
import poussecafe.runtime.OriginalAndMarshaledMessage;

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

        MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                .consumptionId(consumptionId)
                .listener(listener)
                .messageConsumptionHandler(messageConsumptionHandler)
                .receivedMessage(receivedMessage)
                .failFast(failFast)
                .build();

        executeInApmTransaction(executor);

        return executor.status() == MessageListenerExecutionStatus.EXPECTING_RETRY;
    }

    private MessageListenerExecutionStatus executeInApmTransaction(MessageListenerExecutor executor) {
        String transactionName = executor.listener().shortId();
        ApmTransaction apmTransaction = applicationPerformanceMonitoring.startTransaction(transactionName);
        executor.executeListener();
        endOrIgnoreApmTransaction(executor, apmTransaction);
        return executor.status();
    }

    private void endOrIgnoreApmTransaction(MessageListenerExecutor executor, ApmTransaction apmTransaction) {
        MessageListenerExecutionStatus status = executor.status();
        if(status == MessageListenerExecutionStatus.SUCCESS) {
            apmTransaction.setResult(ApmTransactionResults.SUCCESS);
        } else if(status == MessageListenerExecutionStatus.EXPECTING_RETRY) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
        } else if(status == MessageListenerExecutionStatus.IGNORED) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, executor.executionError().orElseThrow().getClass().getSimpleName());
        } else if(status == MessageListenerExecutionStatus.FAILED) {
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            apmTransaction.captureException(executor.executionError().orElseThrow());
        } else {
            throw new IllegalArgumentException("Unsupported listener execution status " + status);
        }
        apmTransaction.end();
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
