package poussecafe.jackson;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JacksonMessageAdapterTest {

    @Test
    public void jacksonSuccessfulEndToEnd() {
        givenInitialMessage();
        whenAdaptingEndToEnd();
        thenProducedMessageIsSameAsInitial();
    }

    private void givenInitialMessage() {
        initialMessage = new SimpleMessage();
        initialMessage.payload = "payload";
        initialMessage.date = LocalDate.now();
        initialMessage.bigDecimal = new BigDecimal("42.0000");
    }

    private SimpleMessage initialMessage;

    private void whenAdaptingEndToEnd() {
        String serializedMessage = adapter.adaptMessage(initialMessage);
        producedMessage = (SimpleMessage) adapter.adaptSerializedMessage(serializedMessage);
    }

    private JacksonMessageAdapter adapter = new JacksonMessageAdapter();

    private SimpleMessage producedMessage;

    private void thenProducedMessageIsSameAsInitial() {
        assertTrue(initialMessage.equals(producedMessage));
    }
}
