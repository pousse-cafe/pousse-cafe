package poussecafe.configuration;

import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryRepository;

public class ConsequenceJournalEntryConfiguration extends
StorableConfiguration<JournalEntryKey, JournalEntry, JournalEntry.Data, JournalEntryFactory, JournalEntryRepository> {

    public ConsequenceJournalEntryConfiguration() {
        super(JournalEntry.class, JournalEntry.Data.class, JournalEntryFactory.class, JournalEntryRepository.class);
    }

}
