package poussecafe.processing;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.MessageListener;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageBroker {

    public static class Builder {

        private MessageBroker broker = new MessageBroker();

        public Builder messageProcessingThreadsPool(Processor messageProcessingThreadsPool) {
            broker.processor = messageProcessingThreadsPool;
            return this;
        }

        public Builder messageConsumptionContext(MessageConsumptionContext messageConsumptionContext) {
            broker.messageConsumptionContext = messageConsumptionContext;
            return this;
        }

        public Builder listenersSet(ListenersSet listenersSet) {
            broker.listenersSet = listenersSet;
            return this;
        }

        public MessageBroker build() {
            Objects.requireNonNull(broker.processor);
            Objects.requireNonNull(broker.messageConsumptionContext);
            Objects.requireNonNull(broker.listenersSet);

            return broker;
        }
    }

    private MessageBroker() {

    }

    private Processor processor;

    private MessageConsumptionContext messageConsumptionContext;

    public synchronized void dispatch(ReceivedMessage receivedMessage) {
        logger.debug("Handling received message {}", receivedMessage.message().original());

        List<MessageListenersGroup> groups = buildMessageListenerGroups(receivedMessage.message());
        logGroups(groups);
        if(groups.isEmpty()) {
            logger.debug("No groups built for message {}, acking...", receivedMessage.message().original());
            receivedMessage.ack();
        } else {
            processor.submit(receivedMessage, groups);
        }
    }

    private List<MessageListenersGroup> buildMessageListenerGroups(OriginalAndMarshaledMessage message) {
        Collection<MessageListener> listeners = listenersSet.messageListenersOf(message.original().getClass());
        return new MessageListenersGroupsFactory.Builder()
                .messageConsumptionContext(messageConsumptionContext)
                .message(message)
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

    private Logger logger = LoggerFactory.getLogger(getClass());
}
