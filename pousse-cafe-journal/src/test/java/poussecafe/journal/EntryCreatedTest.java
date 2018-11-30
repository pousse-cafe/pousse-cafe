package poussecafe.journal;

import org.junit.Test;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Mockito.verify;

public abstract class EntryCreatedTest extends MessagingJournalTest {

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
        whenLogging();
        thenNewEntryIsAdded();
    }

    protected abstract void givenMessage();

    protected abstract void whenLogging();

    protected void thenNewEntryIsAdded() {
        verify(entryRepository).add(newEntry);
    }
}
