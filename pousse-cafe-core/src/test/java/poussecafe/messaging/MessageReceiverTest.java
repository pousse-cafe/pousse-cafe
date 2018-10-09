package poussecafe.messaging;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static poussecafe.collection.Collections.asSet;

public abstract class MessageReceiverTest {

    private MessageListenerRegistry listenerRegistry;

    private Message message;

    private MessageReceiver receiver;

    private MessageListener listener;

    @Test
    public void receivedMessageIsConsumed() {
        givenMessageQueue();
        whenConsumingMessage();
        thenListenerConsumes();
    }

    private void givenMessageQueue() {
        listenerRegistry = mock(MessageListenerRegistry.class);

        listener = mock(MessageListener.class);
        when(listener.getListenerId()).thenReturn("listenerId");

        message = new TestMessage();
        when(listenerRegistry.getListeners(new MessageListenerRoutingKey(message.getClass())))
                .thenReturn(asSet(listener));

        receiver = newMessageReceiver();
        receiver.setListenerRegistry(listenerRegistry);
    }

    protected abstract MessageReceiver newMessageReceiver();

    private void whenConsumingMessage() {
        receiver.onMessage(message);
    }

    private void thenListenerConsumes() {
        verify(listener).consume(message);
    }
}
