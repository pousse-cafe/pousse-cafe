package poussecafe.messaging;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public abstract class MessageReceiverTest {

    private Message message;

    private MessageBroker messageBroker;

    @Test
    public void receivedMessageIsConsumed() {
        givenMessageConsumer();
        whenConsumingMessage();
        thenListenerConsumes();
    }

    private void givenMessageConsumer() {
        message = message();
        messageBroker = mock(MessageBroker.class);
        connection = messaging().connect(messageBroker);
    }

    protected abstract Message message();

    protected abstract Messaging messaging();

    private MessagingConnection connection;

    @SuppressWarnings("unchecked")
    private void whenConsumingMessage() {
        envelope = envelope(message);
        serializedMessage = serializedMessage(envelope);
        payload = new OriginalAndMarshaledMessage.Builder()
                .original(message)
                .marshaled(serializedMessage)
                .build();
        connection.messageReceiver().onMessage(envelope);
    }

    private Object envelope;

    protected abstract Object envelope(Message message);

    private Object serializedMessage;

    protected abstract Object serializedMessage(Object envelope);

    private OriginalAndMarshaledMessage payload;

    private void thenListenerConsumes() {
        ArgumentCaptor<ReceivedMessage> captor = ArgumentCaptor.forClass(ReceivedMessage.class);
        verify(messageBroker).dispatch(captor.capture());
        assertThat(captor.getValue().message(), is(payload));
    }
}
