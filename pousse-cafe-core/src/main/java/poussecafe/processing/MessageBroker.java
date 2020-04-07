package poussecafe.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.processing.MessageToProcess.Callback;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageBroker {

    public static class Builder {

        private MessageBroker broker = new MessageBroker();

        public Builder messageProcessingThreadsPool(MessageProcessingThreadPool messageProcessingThreadsPool) {
            broker.messageProcessingThreadsPool = messageProcessingThreadsPool;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            broker.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            broker.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder listenersSet(ListenersSet listenersSet) {
            broker.listenersSet = listenersSet;
            return this;
        }

        public MessageBroker build() {
            Objects.requireNonNull(broker.messageProcessingThreadsPool);
            Objects.requireNonNull(broker.messageConsumptionHandler);
            Objects.requireNonNull(broker.applicationPerformanceMonitoring);
            Objects.requireNonNull(broker.listenersSet);

            broker.processingThreadSelector =
                    new DefaultProcessingThreadSelector(broker.messageProcessingThreadsPool.size());

            return broker;
        }
    }

    private MessageBroker() {

    }

    private MessageProcessingThreadPool messageProcessingThreadsPool;

    private MessageConsumptionHandler messageConsumptionHandler;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    public synchronized void dispatch(ReceivedMessage receivedMessage) {
        logger.debug("Handling received message {}", receivedMessage.message().original());

        List<MessageListenersGroup> groups = buildMessageListenerGroups(receivedMessage.message());
        logGroups(groups);
        if(groups.isEmpty()) {
            logger.debug("No groups built for message {}, acking...", receivedMessage.message().original());
            receivedMessage.ack();
        } else {
            var receivedMessageProcessingState = new ReceivedMessageProcessingState.Builder()
                    .receivedMessage(receivedMessage)
                    .firstId(nextReceivedMessageId)
                    .sequenceLength(groups.size())
                    .build();

            for(MessageListenersGroup group : groups) {
                var receivedMessageId = nextReceivedMessageId++;
                inProgressProcessingStates.put(receivedMessageId, receivedMessageProcessingState);
                MessageToProcess messageToProcess = new MessageToProcess.Builder()
                        .receivedMessageId(receivedMessageId)
                        .receivedMessagePayload(group)
                        .callback(callback)
                        .build();

                int threadId = processingThreadSelector.selectFor(group);
                messageProcessingThreadsPool.submit(threadId, messageToProcess);
            }
        }
    }

    private List<MessageListenersGroup> buildMessageListenerGroups(OriginalAndMarshaledMessage message) {
        Collection<MessageListener> listeners = listenersSet.messageListenersOf(message.original().getClass());
        return new MessageListenersGroupsFactory.Builder()
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .message(message)
                .messageConsumptionHandler(messageConsumptionHandler)
                .build()
                .buildMessageListenerGroups(listeners);
    }

    private ListenersSet listenersSet;

    private void logGroups(List<MessageListenersGroup> groups) {
        if(logger.isDebugEnabled()) {
            logger.debug("Built {} groups:", groups.size());
            for(MessageListenersGroup group : groups) {
                logger.debug("    group {}", group.aggregateRootClass());
                for(MessageListener listener : group.listeners()) {
                    logger.debug("        - {}", listener.shortId());
                }
            }
        }
    }

    private long nextReceivedMessageId = 0;

    private MessageBrokerCallback callback = new MessageBrokerCallback();

    private ProcessingThreadSelector processingThreadSelector;

    private synchronized void signalProcessed(int threadId, MessageToProcess processedMessage) { // NOSONAR - synchronized
        long receivedMessageId = processedMessage.receivedMessageId();
        var processingProgress = inProgressProcessingStates.remove(receivedMessageId);
        if(processingProgress == null) {
            throw new IllegalArgumentException("No processing state available");
        } else {
            processingProgress.ackReceivedMessageId(processedMessage.receivedMessageId());
            if(processingProgress.isCompleted()) {
                logger.debug("Processing of message {} completed, acking...", receivedMessageId);
                processingProgress.receivedMessage().ack();
            }
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<Long, ReceivedMessageProcessingState> inProgressProcessingStates = new HashMap<>();

    private class MessageBrokerCallback implements Callback {

        @Override
        public void signalProcessed(int threadId, MessageToProcess processedMessage) {
            MessageBroker.this.signalProcessed(threadId, processedMessage);
        }
    }
}
