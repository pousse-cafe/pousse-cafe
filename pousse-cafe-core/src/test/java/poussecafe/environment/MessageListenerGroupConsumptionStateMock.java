package poussecafe.environment;

import org.mockito.Mockito;
import org.slf4j.Logger;
import poussecafe.messaging.Message;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.mockito.Mockito.when;

public class MessageListenerGroupConsumptionStateMock {

    public static MessageListenerGroupConsumptionState mock(Class<? extends Message> messageClass) {
        var state = Mockito.mock(MessageListenerGroupConsumptionState.class);
        when(state.consumptionId()).thenReturn("consumptionId");
        when(state.isFirstConsumption()).thenReturn(true);

        var originalAndMarshaled = Mockito.mock(OriginalAndMarshaledMessage.class);
        when(state.message()).thenReturn(originalAndMarshaled);
        var original = Mockito.mock(messageClass);
        when(originalAndMarshaled.original()).thenReturn(original);

        var logger = Mockito.mock(Logger.class);
        when(state.processorLogger()).thenReturn(logger);

        return state;
    }

    private MessageListenerGroupConsumptionStateMock() {

    }
}
