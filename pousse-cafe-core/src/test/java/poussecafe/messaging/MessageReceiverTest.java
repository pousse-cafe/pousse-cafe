package poussecafe.messaging;

import org.junit.Test;
import poussecafe.configuration.TestCommand;
import poussecafe.journal.MessagingJournal;
import poussecafe.journal.SuccessfulConsumption;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.Message;
import poussecafe.messaging.Queue;

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

        Queue source = Queue.DEFAULT_COMMAND_QUEUE;
        message = new TestCommand();
        when(listenerRegistry.getListeners(new MessageListenerRoutingKey(source, message.getClass())))
        .thenReturn(asSet(listener));

        receiver = newMessageReceiver(source);
        receiver.setMessagingJournal(messagingJournal);
        receiver.setListenerRegistry(listenerRegistry);
    }

    protected abstract MessageReceiver newMessageReceiver(Queue source);

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
        verify(messagingJournal).logSuccessfulConsumption(listener.getListenerId(),
                new SuccessfulConsumption(message));
    }

}
