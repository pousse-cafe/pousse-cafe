package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.util.ExponentialBackoff;

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

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            consumption.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        public MessageConsumption build() {
            Objects.requireNonNull(consumption.consumptionId);
            Objects.requireNonNull(consumption.listenersPartition);
            Objects.requireNonNull(consumption.messageConsumptionHandler);
            Objects.requireNonNull(consumption.applicationPerformanceMonitoring);
            Objects.requireNonNull(consumption.logger);
            Objects.requireNonNull(consumption.message);
            Objects.requireNonNull(consumption.messageConsumptionConfiguration);

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
        logGroup(groups);
        if(!groups.isEmpty()) {
            List<MessageListenerGroup> toRetryInitially = consumeMessageOrRetryGroups(groups);
            if(!toRetryInitially.isEmpty()) {
                retryConsumption(toRetryInitially);
            }
        }
        logger.debug("Message {} handled (consumption ID {})", message.original(), consumptionId);
    }

    private void logGroup(List<MessageListenerGroup> groups) {
        if(logger.isDebugEnabled()) {
            logger.debug("Built {} groups:", groups.size());
            for(MessageListenerGroup group : groups) {
                logger.debug("    group {}", group.aggregateRootClass());
                for(MessageListener listener : group.listeners()) {
                    logger.debug("        - {}", listener.shortId());
                }
            }
        }
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
            boolean mustRetry = consumeMessage(group);
            if(mustRetry) {
                toRetry.add(group);
            }
        }
        return toRetry;
    }

    private boolean consumeMessage(MessageListenerGroup group) {
        MessageListenerGroupConsumptionState consumptionState = messageConsumptionState.buildMessageListenerGroupState();
        List<MessageListenerConsumptionReport> reports = group.consumeMessageOrRetry(consumptionState);
        messageConsumptionState.update(reports);
        return reports.stream().anyMatch(MessageListenerConsumptionReport::mustRetry);
    }

    private void retryConsumption(List<MessageListenerGroup> toRetryInitially) {
        messageConsumptionState.isFirstConsumption(false);
        ExponentialBackoff exponentialBackoff = new ExponentialBackoff.Builder()
                .slotTime(messageConsumptionConfiguration.backOffSlotTime())
                .ceiling(messageConsumptionConfiguration.backOffCeiling())
                .build();
        int retry = 1;
        List<MessageListenerGroup> toRetry = toRetryInitially;
        long cumulatedWaitTime = 0;
        while(!toRetry.isEmpty() && retry <= messageConsumptionConfiguration.maxConsumptionRetries()) {
            long waitTime = (long) Math.ceil(exponentialBackoff.nextValue());
            logger.warn("Retrying consumption of {} for {} groups in {} ms", message.original().getClass().getSimpleName(), toRetry.size(), waitTime);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted during backoff");
                Thread.currentThread().interrupt();
                break;
            }
            toRetry = consumeMessageOrRetryGroups(toRetry);
            cumulatedWaitTime += waitTime;
            ++retry;
        }
        if(!toRetry.isEmpty()) {
            logger.error("Reached max. # of retries ({}), giving up handling of {} with {} remaining groups", messageConsumptionConfiguration.maxConsumptionRetries(), message.original().getClass().getName(), toRetry.size());
            logger.error("Unhandled message: {}", message.marshaled());
        } else {
            logger.info("Message {} successfully consumed after {} retries ({} ms cumulated wait time)", message.original().getClass().getSimpleName(), (retry - 1), cumulatedWaitTime);
        }
    }

    private MessageConsumptionConfiguration messageConsumptionConfiguration;

    protected Logger logger;

    private MessageConsumptionState messageConsumptionState;

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
