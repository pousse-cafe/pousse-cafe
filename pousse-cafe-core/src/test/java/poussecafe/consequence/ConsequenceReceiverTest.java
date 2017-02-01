package poussecafe.consequence;

import org.junit.Test;
import poussecafe.configuration.TestCommand;
import poussecafe.journal.ConsequenceJournal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static poussecafe.collection.Collections.asSet;

public abstract class ConsequenceReceiverTest {

    private ConsequenceJournal consequenceJournal;

    private ConsequenceListenerRegistry listenerRegistry;

    private Consequence consequence;

    private ConsequenceReceiver receiver;

    private ConsequenceListener listener;

    @Test
    public void receivedConsequenceIsConsumed() {
        givenConsequenceQueue();
        whenConsumingConsequence();
        thenListenerConsumes();
    }

    private void givenConsequenceQueue() {
        consequenceJournal = mock(ConsequenceJournal.class);
        listenerRegistry = mock(ConsequenceListenerRegistry.class);

        listener = mock(ConsequenceListener.class);
        when(listener.getListenerId()).thenReturn("listenerId");

        Source source = Source.DEFAULT_COMMAND_SOURCE;
        consequence = new TestCommand();
        when(listenerRegistry.getListeners(new ConsequenceListenerRoutingKey(source, consequence.getClass())))
        .thenReturn(asSet(listener));

        receiver = newConsequenceReceiver(source);
        receiver.setConsequenceJournal(consequenceJournal);
        receiver.setListenerRegistry(listenerRegistry);
    }

    protected abstract ConsequenceReceiver newConsequenceReceiver(Source source);

    private void whenConsumingConsequence() {
        receiver.onConsequence(consequence);
    }

    private void thenListenerConsumes() {
        verify(listener).consume(consequence);
    }

    @Test
    public void journalIsUpdatedUponSuccessfulConsumtion() {
        givenConsequenceQueue();
        whenConsumingConsequence();
        thenJournalUpdatedWithSuccess();
    }

    private void thenJournalUpdatedWithSuccess() {
        verify(consequenceJournal).logSuccessfulConsumption(listener.getListenerId(), consequence);
    }

}
