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

    private void whenConsumingMessage() {
        serializedMessage = serializedMessage(message);
        payload = new OriginalAndMarshaledMessage.Builder()
                .original(message)
                .marshaled(serializedMessage)
                .build();
        connection.messageReceiver().onMessage(new ReceivedMessage.Builder()
                .payload(payload)
                .acker(() -> {})
                .build());
    }

    private Object serializedMessage;

    protected abstract Object serializedMessage(Message message);

    private OriginalAndMarshaledMessage payload;

    private void thenListenerConsumes() {
        ArgumentCaptor<ReceivedMessage> captor = ArgumentCaptor.forClass(ReceivedMessage.class);
        verify(messageBroker).dispatch(captor.capture());
        assertThat(captor.getValue().message(), is(payload));
    }
}
