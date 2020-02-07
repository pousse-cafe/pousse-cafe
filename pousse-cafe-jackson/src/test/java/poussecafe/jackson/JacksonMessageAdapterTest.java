package poussecafe.jackson;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
        initialMessage.bigDecimal = new BigDecimal(bigNumberString);
        initialMessage.polymorphicPayload = OffsetDateTime.now();
        initialMessage.intPrimitive = 42;
    }

    private String bigNumberString = "42424242424242424242.424242424242424242";

    private SimpleMessage initialMessage;

    private void whenAdaptingEndToEnd() {
        whenSerializing();
        whenUnserializing();
    }

    private void whenUnserializing() {
        producedMessage = (SimpleMessage) adapter.adaptSerializedMessage(serializedMessage);
    }

    private void whenSerializing() {
        serializedMessage = adapter.adaptMessage(initialMessage);
    }

    private String serializedMessage;

    private JacksonMessageAdapter adapter = new JacksonMessageAdapter();

    private SimpleMessage producedMessage;

    private void thenProducedMessageIsSameAsInitial() {
        assertThat(producedMessage, equalTo(initialMessage));
    }

    @Test
    public void bigDecimalSerializedAsString() {
        givenInitialMessage();
        whenSerializing();
        thenBigDecimalSerializedAsString();
    }

    private void thenBigDecimalSerializedAsString() {
        assertTrue(serializedMessage.contains("\"" + bigNumberString + "\""));
    }
}
