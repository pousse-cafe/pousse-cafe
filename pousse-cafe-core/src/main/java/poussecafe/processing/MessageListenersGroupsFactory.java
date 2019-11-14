package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.Arrays.asList;

public class MessageListenersGroupsFactory {

    public static class Builder {

        private MessageListenersGroupsFactory factory = new MessageListenersGroupsFactory();

        public Builder consumptionId(String consumptionId) {
            factory.consumptionId = consumptionId;
            return this;
        }

        public Builder failFast(boolean failFast) {
            factory.failFast = failFast;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            factory.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            factory.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder message(OriginalAndMarshaledMessage message) {
            factory.message = message;
            return this;
        }

        public MessageListenersGroupsFactory build() {
            Objects.requireNonNull(factory.consumptionId);
            Objects.requireNonNull(factory.message);
            Objects.requireNonNull(factory.messageConsumptionHandler);
            Objects.requireNonNull(factory.applicationPerformanceMonitoring);

            return factory;
        }
    }

    private MessageListenersGroupsFactory() {

    }

    @SuppressWarnings("rawtypes")
    public List<MessageListenerGroup> buildMessageListenerGroups(Collection<MessageListener> listeners) {
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

        List<MessageListenerGroup> groups = new ArrayList<>();
        for(Entry<Class, List<MessageListener>> entry : listenersPerAggregateRootClass.entrySet()) {
            MessageListenerGroup group = new MessageListenerGroup.Builder()
                    .listeners(entry.getValue())
                    .aggregateRootClass(Optional.of(entry.getKey()))
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .consumptionId(consumptionId)
                    .message(message)
                    .messageConsumptionHandler(messageConsumptionHandler)
                    .failFast(failFast)
                    .build();
            groups.add(group);
        }

        customListeners.sort(null);
        for(MessageListener customListener : customListeners) {
            MessageListenerGroup group = new MessageListenerGroup.Builder()
                    .listeners(asList(customListener))
                    .aggregateRootClass(Optional.empty())
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .consumptionId(consumptionId)
                    .message(message)
                    .messageConsumptionHandler(messageConsumptionHandler)
                    .failFast(failFast)
                    .build();
            groups.add(group);
        }

        return groups;
    }

    private String consumptionId;

    private OriginalAndMarshaledMessage message;

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
