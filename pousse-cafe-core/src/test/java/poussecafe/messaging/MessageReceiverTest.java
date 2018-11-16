package poussecafe.messaging;

import org.junit.Test;
import poussecafe.context.MessageConsumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public abstract class MessageReceiverTest {

    private Message message;

    private MessageConsumer messageConsumer;

    @Test
    public void receivedMessageIsConsumed() {
        givenMessageConsumer();
        whenConsumingMessage();
        thenListenerConsumes();
    }

    private void givenMessageConsumer() {
        message = new TestMessage();
        messageConsumer = mock(MessageConsumer.class);
        messaging().configure(messageConsumer);
    }

    protected abstract Messaging messaging();

    private void whenConsumingMessage() {
        messaging().messageReceiver().onMessage(message);
    }

    private void thenListenerConsumes() {
        verify(messageConsumer).consumeMessage(message);
    }
}
