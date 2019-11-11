package poussecafe.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import poussecafe.environment.ConsumptionStatus;
import poussecafe.environment.MessageConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.MessageListenerPriority;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageConsumptionState {

    public MessageConsumptionState(OriginalAndMarshaledMessage message) {
        Objects.requireNonNull(message);
        this.message = message;
    }

    private OriginalAndMarshaledMessage message;

    public void isFirstConsumption(boolean isFirstConsumption) {
        this.isFirstConsumption = isFirstConsumption;
    }

    private boolean isFirstConsumption = true;

    public MessageListenerGroupConsumptionState buildMessageListenerGroupState(MessageListenerGroup listenerGroup) {
        MessageListenerGroupConsumptionState.Builder stateBuilder = new MessageListenerGroupConsumptionState.Builder()
            .message(message)
            .isFirstConsumption(isFirstConsumption);

        @SuppressWarnings("rawtypes")
        Optional<Class> listenerGroupAggregateRoot = listenerGroup.aggregateRootClass();
        if(listenerGroupAggregateRoot.isPresent()) {
            stateBuilder.aggregateRootClass(listenerGroupAggregateRoot);
            Map<Object, ConsumptionStatus> aggregateUpdateStatus = aggregatesUpdatesState.get(listenerGroupAggregateRoot.get());
            if(aggregateUpdateStatus != null) {
                for(Entry<Object, ConsumptionStatus> entry : aggregateUpdateStatus.entrySet()) {
                    stateBuilder.aggregateStatus(entry.getKey(), entry.getValue());
                }
            }
            return stateBuilder.build();
        }

        return stateBuilder.build();
    }

    @SuppressWarnings("rawtypes")
    private Map<Class, Map<Object, ConsumptionStatus>> aggregatesUpdatesState = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public void update(List<MessageConsumptionReport> reports) {
        for(MessageConsumptionReport report : reports) {
            Optional<Class> aggregateRootClass = report.aggregateType();
            if(aggregateRootClass.isPresent() && report.listenerType() == MessageListenerPriority.AGGREGATE) {
                Map<Object, ConsumptionStatus> state = aggregatesUpdatesState.computeIfAbsent(aggregateRootClass.get(), key -> new HashMap<>());
                setStatus(state, report.successfulAggregatesIds(), ConsumptionStatus.SUCCESS);
                setStatus(state, report.failedAggregatesIds(), ConsumptionStatus.FAILURE);
                setStatus(state, report.skippedAggregatesIds(), ConsumptionStatus.IGNORED);
                setStatus(state, report.aggregateIdsToRetry(), ConsumptionStatus.RETRY);
            }
        }
    }

    private void setStatus(Map<Object, ConsumptionStatus> state, Collection<Object> ids, ConsumptionStatus status) {
        for(Object id : ids) {
            state.put(id, status);
        }
    }
}
