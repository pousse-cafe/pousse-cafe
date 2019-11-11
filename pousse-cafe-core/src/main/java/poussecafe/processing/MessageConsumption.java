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
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageConsumptionReport;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.Arrays.asList;

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

            consumption.messageConsumptionState = new MessageConsumptionState(consumption.message);

            return consumption;
        }
    }

    private MessageConsumption() {

    }

    private String consumptionId;

    private OriginalAndMarshaledMessage message;

    public void execute() {
        logger.debug("Handling received message {}", message.original());
        List<MessageListenerGroup> listeners = buildMessageListenerGroups();
        if(!listeners.isEmpty()) {
            logger.debug("  Found {} listeners", listeners.size());
            List<MessageListenerGroup> toRetryInitially = consumeMessageOrRetryListeners(listeners);
            if(!toRetryInitially.isEmpty()) {
                retryConsumption(toRetryInitially);
            }
        }
        logger.debug("Message {} handled (consumption ID {})", message.original(), consumptionId);
    }

    @SuppressWarnings("rawtypes")
    private List<MessageListenerGroup> buildMessageListenerGroups() {
        Collection<MessageListener> listeners = listenersPartition.partitionListenersSet()
                .messageListenersOf(message.original().getClass());
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

    private ListenersSetPartition listenersPartition;

    private List<MessageListenerGroup> consumeMessageOrRetryListeners(List<MessageListenerGroup> listeners) {
        List<MessageListenerGroup> toRetry = new ArrayList<>();
        for (MessageListenerGroup listener : listeners) {
            if(consumeMessageOrRetry(listener)) {
                toRetry.add(listener);
            }
        }
        return toRetry;
    }

    private void retryConsumption(List<MessageListenerGroup> toRetryInitially) {
        messageConsumptionState.isFirstConsumption(false);
        int retry = 1;
        List<MessageListenerGroup> toRetry = toRetryInitially;
        while(!toRetry.isEmpty() && retry <= MAX_RETRIES) {
            logger.debug("    Try #{} for {} listeners", retry, toRetry.size());
            toRetry = consumeMessageOrRetryListeners(toRetry);
            ++retry;
        }
    }

    private static final int MAX_RETRIES = 10;

    protected Logger logger;

    private boolean consumeMessageOrRetry(MessageListenerGroup listener) {
        MessageListenerGroupConsumptionState consumptionState = messageConsumptionState.buildMessageListenerGroupState(listener);
        List<MessageConsumptionReport> reports = listener.consumeMessageOrRetry(consumptionState);
        messageConsumptionState.update(reports);
        return reports.stream().anyMatch(MessageConsumptionReport::mustRetry);
    }

    private MessageConsumptionState messageConsumptionState;

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
