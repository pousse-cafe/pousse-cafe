package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageListenerGroupConsumptionState {

    public static class Builder {

        private MessageListenerGroupConsumptionState  state = new MessageListenerGroupConsumptionState();

        public Builder message(OriginalAndMarshaledMessage message) {
            state.message = message;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateRootClass(Optional<Class> aggregateRootClass) {
            state.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public Builder isFirstConsumption(boolean isFirstConsumption) {
            state.isFirstConsumption = isFirstConsumption;
            return this;
        }

        public Builder idsToRetry(Set<Object> idsToRetry) {
            state.idsToRetry = idsToRetry;
            return this;
        }

        public Builder processorLogger(Logger processorLogger) {
            state.processorLogger = processorLogger;
            return this;
        }

        public MessageListenerGroupConsumptionState build() {
            Objects.requireNonNull(state.message);
            Objects.requireNonNull(state.aggregateRootClass);
            Objects.requireNonNull(state.processorLogger);
            return state;
        }
    }

    private MessageListenerGroupConsumptionState() {

    }

    private OriginalAndMarshaledMessage message;

    public OriginalAndMarshaledMessage message() {
        return message;
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateRootClass = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<Class> aggregateRootClass() {
        return aggregateRootClass;
    }

    public boolean isFirstConsumption() {
        return isFirstConsumption;
    }

    private boolean isFirstConsumption;

    public Set<Object> idsToRetry() {
        return idsToRetry;
    }

    private Set<Object> idsToRetry;

    public Logger processorLogger() {
        return processorLogger;
    }

    private Logger processorLogger;
}
