package poussecafe.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

        public Builder aggregateUpdateStatus(Object id, ConsumptionStatus status) {
            state.aggregateUpdateStatusPerInstance.put(id, status);
            return this;
        }

        public Builder isFirstConsumption(boolean isFirstConsumption) {
            state.isFirstConsumption = isFirstConsumption;
            return this;
        }

        public MessageListenerGroupConsumptionState build() {
            Objects.requireNonNull(state.message);
            Objects.requireNonNull(state.aggregateRootClass);
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

    private Map<Object, ConsumptionStatus> aggregateUpdateStatusPerInstance = new HashMap<>();

    public boolean mustRun(MessageListener listener) {
        return isFirstConsumption() || listener.priority() == MessageListenerPriority.AGGREGATE;
    }

    public boolean mustRunAggregateListener(Object id) {
        ConsumptionStatus consumptionStatus = aggregateUpdateStatusPerInstance.get(id);
        return consumptionStatus == null
                || consumptionStatus == ConsumptionStatus.RETRY;
    }

    public boolean isFirstConsumption() {
        return isFirstConsumption;
    }

    private boolean isFirstConsumption;
}
