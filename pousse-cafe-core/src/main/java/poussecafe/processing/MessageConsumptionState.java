package poussecafe.processing;

import java.util.List;
import java.util.Objects;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
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

    public MessageListenerGroupConsumptionState buildMessageListenerGroupState() {
        return new MessageListenerGroupConsumptionState.Builder()
            .message(message)
            .isFirstConsumption(isFirstConsumption)
            .build();
    }

    public void update(List<MessageListenerConsumptionReport> reports) {
        // Do nothing for the moment
    }
}
