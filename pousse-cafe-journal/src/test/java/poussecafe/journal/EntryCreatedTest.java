package poussecafe.journal;

import org.junit.Test;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryRepository;

import static org.junit.Assert.assertNotNull;

public abstract class EntryCreatedTest extends MessagingJournalTest {

    protected JournalEntry newEntry;

    protected JournalEntry existingEntry;

    @Test
    public void entryCreatedOnSuccess() {
        givenMessage();
        whenLogging();
        thenNewEntryIsAdded();
    }

    protected abstract void givenMessage();

    protected abstract void whenLogging();

    protected void thenNewEntryIsAdded() {
        JournalEntry entry = journalEntryRepository.get(id);
        assertNotNull(entry);
    }

    private JournalEntryRepository journalEntryRepository;
}
