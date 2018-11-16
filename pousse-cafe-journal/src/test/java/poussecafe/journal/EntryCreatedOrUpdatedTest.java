package poussecafe.journal;

import org.junit.Test;
import org.mockito.InOrder;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class EntryCreatedOrUpdatedTest extends MessagingJournalTest {

    protected JournalEntry newEntry;

    protected JournalEntry existingEntry;

    @Override
    protected TransactionRunner transactionRunner() {
        return new NoTransactionRunner();
    }

    @Test
    public void entryCreatedOnSuccess() {
        givenConfiguredMessagingJournal();
        givenMessage();
        givenNoEntryYetInJournal();
        whenLogging();
        thenNewEntryIsAdded();
    }

    protected abstract void givenMessage();

    protected void givenNoEntryYetInJournal() {
        newEntry = mock(JournalEntry.class);
        when(entryFactory.buildEntryForSentMessage(key, "{\"@class\":\"" + message.getClass().getName() + "\"}")).thenReturn(newEntry);
    }

    protected abstract void whenLogging();

    protected void thenNewEntryIsAdded() {
        verify(entryRepository).add(newEntry);
    }

    @Test
    public void entryUpdatedOnSuccess() {
        givenConfiguredMessagingJournal();
        givenMessage();
        givenExistingEntryInJournal();
        whenLogging();
        thenExistingEntryIsUpdated();
    }

    protected void givenExistingEntryInJournal() {
        existingEntry = mock(JournalEntry.class);
        givenKey();
        when(entryRepository.find(key)).thenReturn(existingEntry);
    }

    private void thenExistingEntryIsUpdated() {
        InOrder sequence = inOrder(existingEntry, entryRepository);
        verifyLog(sequence);
        sequence.verify(entryRepository).update(existingEntry);
    }

    protected abstract void verifyLog(InOrder sequence);

}
