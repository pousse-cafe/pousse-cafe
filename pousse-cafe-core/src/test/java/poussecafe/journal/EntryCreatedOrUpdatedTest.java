package poussecafe.journal;

import org.junit.Test;
import org.mockito.InOrder;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class EntryCreatedOrUpdatedTest extends ConsequenceJournalTest {

    protected Entry newEntry;

    protected Entry existingEntry;

    @Override
    protected TransactionRunner transactionRunner() {
        return new NoTransactionRunner();
    }

    @Test
    public void entryCreatedOnSuccess() {
        givenConfiguredConsequenceJournal();
        givenConsequence();
        givenNoEntryYetInJournal();
        whenLogging();
        thenNewEntryIsAdded();
    }

    protected abstract void givenConsequence();

    protected void givenNoEntryYetInJournal() {
        newEntry = mock(Entry.class);
        EntryKey key = new EntryKey(consequence.getId(), listenerId);
        when(entryFactory.buildEntryForEmittedConsequence(key, consequence)).thenReturn(newEntry);
    }

    protected abstract void whenLogging();

    protected void thenNewEntryIsAdded() {
        verify(entryRepository).add(newEntry);
    }

    @Test
    public void entryUpdatedOnSuccess() {
        givenConfiguredConsequenceJournal();
        givenConsequence();
        givenExistingEntryInJournal();
        whenLogging();
        thenExistingEntryIsUpdated();
    }

    protected void givenExistingEntryInJournal() {
        existingEntry = mock(Entry.class);
        EntryKey key = new EntryKey(consequence.getId(), listenerId);
        when(entryRepository.find(key)).thenReturn(existingEntry);
    }

    private void thenExistingEntryIsUpdated() {
        InOrder sequence = inOrder(existingEntry, entryRepository);
        verifyLog(sequence);
        sequence.verify(entryRepository).update(existingEntry);
    }

    protected abstract void verifyLog(InOrder sequence);

}
