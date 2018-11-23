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
        message = message();
        messageConsumer = mock(MessageConsumer.class);
        connection = messaging().connect(messageConsumer);
    }

    protected abstract Message message();

    protected abstract Messaging messaging();

    private MessagingConnection connection;

    private void whenConsumingMessage() {
        connection.messageReceiver().onMessage(serializedMessage(message));
    }

    protected abstract Object serializedMessage(Message message);

    private void thenListenerConsumes() {
        verify(messageConsumer).consumeMessage(message);
    }
}
