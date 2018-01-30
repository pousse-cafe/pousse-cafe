package poussecafe.messaging;

import org.junit.Test;
import poussecafe.journal.MessagingJournal;
import poussecafe.journal.SuccessfulConsumption;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static poussecafe.collection.Collections.asSet;

public abstract class MessageReceiverTest {

    private MessagingJournal messagingJournal;

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
        messagingJournal = mock(MessagingJournal.class);
        listenerRegistry = mock(MessageListenerRegistry.class);

        listener = mock(MessageListener.class);
        when(listener.getListenerId()).thenReturn("listenerId");

        message = new TestMessage();
        when(listenerRegistry.getListeners(new MessageListenerRoutingKey(message.getClass())))
                .thenReturn(asSet(listener));

        receiver = newMessageReceiver();
        receiver.setMessagingJournal(messagingJournal);
        receiver.setListenerRegistry(listenerRegistry);
    }

    protected abstract MessageReceiver newMessageReceiver();

    private void whenConsumingMessage() {
        receiver.onMessage(message);
    }

    private void thenListenerConsumes() {
        verify(listener).consume(message);
    }

    @Test
    public void journalIsUpdatedUponSuccessfulConsumtion() {
        givenMessageQueue();
        whenConsumingMessage();
        thenJournalUpdatedWithSuccess();
    }

    private void thenJournalUpdatedWithSuccess() {
        verify(messagingJournal).logSuccessfulConsumption(listener.getListenerId(), new SuccessfulConsumption(message));
    }

}
