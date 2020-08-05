package poussecafe.environment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.processing.MessageListenersGroup;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageListenerGroupConsumptionState {

    public static class Builder {

        private MessageListenerGroupConsumptionState  state = new MessageListenerGroupConsumptionState();

        public Builder message(OriginalAndMarshaledMessage message) {
            state.message = message;
            return this;
        }

        public Builder messageListenersGroup(MessageListenersGroup messageListenersGroup) {
            state.messageListenersGroup = messageListenersGroup;
            return this;
        }

        public Builder isFirstConsumption(boolean isFirstConsumption) {
            state.isFirstConsumption = isFirstConsumption;
            return this;
        }

        public Builder toUpdate(Set<Object> toUpdate) {
            state.toUpdate = toUpdate;
            return this;
        }

        public Builder processorLogger(Logger processorLogger) {
            state.processorLogger = processorLogger;
            return this;
        }

        public Builder consumptionId(String consumptionId) {
            state.consumptionId = consumptionId;
            return this;
        }

        public MessageListenerGroupConsumptionState build() {
            Objects.requireNonNull(state.message);
            Objects.requireNonNull(state.messageListenersGroup);
            Objects.requireNonNull(state.processorLogger);
            Objects.requireNonNull(state.consumptionId);
            return state;
        }
    }

    private MessageListenerGroupConsumptionState() {

    }

    private OriginalAndMarshaledMessage message;

    public OriginalAndMarshaledMessage message() {
        return message;
    }

    private MessageListenersGroup messageListenersGroup;

    public MessageListenersGroup messageListenersGroup() {
        return messageListenersGroup;
    }

    public boolean isFirstConsumption() {
        return isFirstConsumption;
    }

    private boolean isFirstConsumption;

    public Set<Object> toUpdate() {
        return toUpdate;
    }

    private Set<Object> toUpdate = new HashSet<>();

    public Logger processorLogger() {
        return processorLogger;
    }

    private Logger processorLogger;

    private String consumptionId;

    public String consumptionId() {
        return consumptionId;
    }
}
