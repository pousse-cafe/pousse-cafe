package poussecafe.processing;

import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.util.ExponentialBackoff;

public class MessageConsumption {

    public static class Builder {

        private MessageConsumption consumption = new MessageConsumption();

        public Builder consumptionId(String consumptionId) {
            consumption.consumptionId = consumptionId;
            return this;
        }

        public Builder processorLogger(Logger processorLogger) {
            consumption.processorLogger = processorLogger;
            return this;
        }

        public Builder messageListenerGroup(MessageListenersGroup messageListenerGroup) {
            consumption.messageListenerGroup = messageListenerGroup;
            return this;
        }

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            consumption.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        public MessageConsumption build() {
            Objects.requireNonNull(consumption.consumptionId);
            Objects.requireNonNull(consumption.processorLogger);
            Objects.requireNonNull(consumption.messageListenerGroup);
            Objects.requireNonNull(consumption.messageConsumptionConfiguration);

            consumption.messageConsumptionState = new MessageConsumptionState.Builder()
                    .consumptionId(consumption.consumptionId)
                    .message(consumption.messageListenerGroup.message())
                    .processorLogger(consumption.processorLogger)
                    .build();

            return consumption;
        }
    }

    private MessageConsumption() {

    }

    private String consumptionId;

    private MessageListenersGroup messageListenerGroup;

    public void execute() {
        boolean toRetryInitially = consumeMessageOrRetryGroup();
        if(toRetryInitially) {
            retryConsumption();
        }
        processorLogger.debug("Message {} handled (consumption ID {})", messageListenerGroup.message().original(), consumptionId);
    }

    private boolean consumeMessageOrRetryGroup() {
        List<MessageListenerConsumptionReport> reports = consumeMessage();
        return reports.stream().anyMatch(MessageListenerConsumptionReport::mustRetry);
    }

    private List<MessageListenerConsumptionReport> consumeMessage() {
        MessageListenerGroupConsumptionState consumptionState = messageConsumptionState
                .buildMessageListenerGroupState(messageListenerGroup);
        List<MessageListenerConsumptionReport> reports = messageListenerGroup.consumeMessageOrRetry(consumptionState);
        messageConsumptionState.update(messageListenerGroup, reports);
        return reports;
    }

    private void retryConsumption() {
        messageConsumptionState.isFirstConsumption(false);
        ExponentialBackoff exponentialBackoff = new ExponentialBackoff.Builder()
                .slotTime(messageConsumptionConfiguration.backOffSlotTime())
                .ceiling(messageConsumptionConfiguration.backOffCeiling())
                .build();
        int retry = 1;
        boolean toRetry = true;
        long cumulatedWaitTime = 0;
        while(toRetry
                && retry <= messageConsumptionConfiguration.maxConsumptionRetries()) {
            long waitTime = (long) Math.ceil(exponentialBackoff.nextValue());
            processorLogger.warn("Retrying consumption of message {} in {} ms",
                    messageListenerGroup.message().original().getClass().getSimpleName(),
                    waitTime);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                processorLogger.error("Thread was interrupted during backoff");
                Thread.currentThread().interrupt();
                break;
            }
            toRetry = consumeMessageOrRetryGroup();
            cumulatedWaitTime += waitTime;
            ++retry;
        }
        if(toRetry) {
            processorLogger.error("Reached max. # of retries ({}), giving up handling of {}",
                    messageConsumptionConfiguration.maxConsumptionRetries(),
                    messageListenerGroup.message().original().getClass().getSimpleName());
            processorLogger.error("Unhandled message: {}", messageListenerGroup.message().marshaled());
        } else {
            processorLogger.info("Message {} successfully consumed after {} retries ({} ms cumulated wait time)",
                    messageListenerGroup.message().original().getClass().getSimpleName(),
                    (retry - 1),
                    cumulatedWaitTime);
        }
    }

    private MessageConsumptionConfiguration messageConsumptionConfiguration;

    protected Logger processorLogger;

    private MessageConsumptionState messageConsumptionState;
}
