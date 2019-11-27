package poussecafe.processing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.Collections.emptySet;

public class MessageConsumptionState {

    public static class Builder {

        private MessageConsumptionState state = new MessageConsumptionState();

        public Builder message(OriginalAndMarshaledMessage message) {
            state.message = message;
            return this;
        }

        public Builder processorLogger(Logger processorLogger) {
            state.processorLogger = processorLogger;
            return this;
        }

        public MessageConsumptionState build() {
            Objects.requireNonNull(state.message);
            Objects.requireNonNull(state.processorLogger);
            return state;
        }
    }

    private MessageConsumptionState() {

    }

    private OriginalAndMarshaledMessage message;

    public void isFirstConsumption(boolean isFirstConsumption) {
        this.isFirstConsumption = isFirstConsumption;
    }

    private boolean isFirstConsumption = true;

    public MessageListenerGroupConsumptionState buildMessageListenerGroupState(MessageListenerGroup group) {
        return new MessageListenerGroupConsumptionState.Builder()
            .message(message)
            .isFirstConsumption(isFirstConsumption)
            .idsToRetry(idsToRetry.getOrDefault(group, emptySet()))
            .processorLogger(processorLogger)
            .build();
    }

    private Logger processorLogger;

    public void update(MessageListenerGroup group, List<MessageListenerConsumptionReport> reports) {
        Set<Object> ids = new HashSet<>();
        for(MessageListenerConsumptionReport report : reports) {
            ids.addAll(report.aggregateIdsToRetry());
        }
        idsToRetry.put(group, ids);
    }

    private Map<MessageListenerGroup, Set<Object>> idsToRetry = new HashMap<>();
}
