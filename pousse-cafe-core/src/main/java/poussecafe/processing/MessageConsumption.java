package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageConsumptionReport;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

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
        List<MessageListenerGroup> groups = buildMessageListenerGroups();
        if(!groups.isEmpty()) {
            logger.debug("  Found {} groups", groups.size());
            List<MessageListenerGroup> toRetryInitially = consumeMessageOrRetryGroups(groups);
            if(!toRetryInitially.isEmpty()) {
                retryConsumption(toRetryInitially);
            }
        }
        logger.debug("Message {} handled (consumption ID {})", message.original(), consumptionId);
    }

    private List<MessageListenerGroup> buildMessageListenerGroups() {
        Collection<MessageListener> listeners = listenersPartition.partitionListenersSet()
                .messageListenersOf(message.original().getClass());
        return new MessageListenersGroupsFactory.Builder()
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .consumptionId(consumptionId)
                .failFast(failFast)
                .message(message)
                .messageConsumptionHandler(messageConsumptionHandler)
                .build()
                .buildMessageListenerGroups(listeners);
    }

    private ListenersSetPartition listenersPartition;

    private List<MessageListenerGroup> consumeMessageOrRetryGroups(List<MessageListenerGroup> groups) {
        List<MessageListenerGroup> toRetry = new ArrayList<>();
        for (MessageListenerGroup group : groups) {
            if(consumeMessageOrRetry(group)) {
                toRetry.add(group);
            }
        }
        return toRetry;
    }

    private boolean consumeMessageOrRetry(MessageListenerGroup group) {
        MessageListenerGroupConsumptionState consumptionState = messageConsumptionState.buildMessageListenerGroupState();
        List<MessageConsumptionReport> reports = group.consumeMessageOrRetry(consumptionState);
        messageConsumptionState.update(reports);
        return reports.stream().anyMatch(MessageConsumptionReport::mustRetry);
    }

    private void retryConsumption(List<MessageListenerGroup> toRetryInitially) {
        logger.warn("Retrying consumption of {}", message.original(), toRetryInitially.size());
        messageConsumptionState.isFirstConsumption(false);
        int retry = 1;
        List<MessageListenerGroup> toRetry = toRetryInitially;
        while(!toRetry.isEmpty() && retry <= MAX_RETRIES) {
            logger.debug("Retrying consumption of {} by {} groups", message.original(), toRetry.size());
            toRetry = consumeMessageOrRetryGroups(toRetry);
            ++retry;
        }
        if(!toRetry.isEmpty()) {
            logger.error("Reached max. # of retries, giving up handling of {} with {} remaining groups", message.original().getClass().getName(), toRetry.size());
            logger.error("Unhandled message: {}", message.marshaled());
        }
    }

    private static final int MAX_RETRIES = 10;

    protected Logger logger;

    private MessageConsumptionState messageConsumptionState;

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
