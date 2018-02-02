package poussecafe.context;

import org.junit.Test;
import poussecafe.journal.SerializedMessage;
import poussecafe.messaging.JacksonMessageAdapter;

import static org.junit.Assert.assertTrue;

public class JacksonMessageAdapterTest {

    @Test
    public void jacksonSuccessfulEndToEnd() {
        givenInitialMessage();
        whenAdaptingEndToEnd();
        thenProducedMessageIsSameAsInitial();
    }

    private void givenInitialMessage() {
        initialMessage = new SimpleMessage("data");
    }

    private SimpleMessage initialMessage;

    private void whenAdaptingEndToEnd() {
        SerializedMessage serializedMessage = adapter.adaptMessage(initialMessage);
        producedMessage = (SimpleMessage) adapter.adaptSerializedMessage(serializedMessage);
    }

    private JacksonMessageAdapter adapter = new JacksonMessageAdapter();

    private SimpleMessage producedMessage;

    private void thenProducedMessageIsSameAsInitial() {
        assertTrue(initialMessage.equals(producedMessage));
    }
}
