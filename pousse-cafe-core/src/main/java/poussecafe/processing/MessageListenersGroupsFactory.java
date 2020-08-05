package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.MessageListener;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.Arrays.asList;

public class MessageListenersGroupsFactory {

    public static class Builder {

        private MessageListenersGroupsFactory factory = new MessageListenersGroupsFactory();

        public Builder messageConsumptionContext(MessageConsumptionContext messageConsumptionContext) {
            factory.messageConsumptionContext = messageConsumptionContext;
            return this;
        }

        public Builder message(OriginalAndMarshaledMessage message) {
            factory.message = message;
            return this;
        }

        public Builder logger(Logger logger) {
            factory.logger = logger;
            return this;
        }

        public MessageListenersGroupsFactory build() {
            Objects.requireNonNull(factory.message);
            Objects.requireNonNull(factory.messageConsumptionContext);
            Objects.requireNonNull(factory.logger);
            return factory;
        }
    }

    private MessageListenersGroupsFactory() {

    }

    @SuppressWarnings("rawtypes")
    public List<MessageListenersGroup> buildMessageListenerGroups(Collection<MessageListener> listeners) {
        List<MessageListener> customListeners = new ArrayList<>();
        Map<Class, List<MessageListener>> listenersPerAggregateRootClass = new HashMap<>();
        for(MessageListener listener : listeners) {
            List<MessageListener> groupList;
            if(listener.aggregateRootClass().isPresent()) {
                groupList = listenersPerAggregateRootClass.computeIfAbsent(listener.aggregateRootClass().orElseThrow(),
                        key -> new ArrayList<>());
            } else {
                groupList = customListeners;
            }
            groupList.add(listener);
        }

        List<MessageListenersGroup> groups = new ArrayList<>();
        for(Entry<Class, List<MessageListener>> entry : listenersPerAggregateRootClass.entrySet()) {
            MessageListenersGroup group = new MessageListenersGroup.Builder()
                    .listeners(entry.getValue())
                    .aggregateRootClass(Optional.of(entry.getKey()))
                    .messageConsumptionContext(messageConsumptionContext)
                    .message(message)
                    .logger(logger)
                    .build();
            groups.add(group);
        }

        customListeners.sort(null);
        for(MessageListener customListener : customListeners) {
            MessageListenersGroup group = new MessageListenersGroup.Builder()
                    .listeners(asList(customListener))
                    .aggregateRootClass(Optional.empty())
                    .messageConsumptionContext(messageConsumptionContext)
                    .message(message)
                    .logger(logger)
                    .build();
            groups.add(group);
        }

        return groups;
    }

    private OriginalAndMarshaledMessage message;

    private MessageConsumptionContext messageConsumptionContext;

    private Logger logger = LoggerFactory.getLogger(getClass());
}
