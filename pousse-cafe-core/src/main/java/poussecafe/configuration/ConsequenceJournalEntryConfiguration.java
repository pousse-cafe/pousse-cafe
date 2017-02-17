package poussecafe.configuration;

import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryRepository;

public abstract class ConsequenceJournalEntryConfiguration extends
        StorableConfiguration<JournalEntryKey, JournalEntry, JournalEntry.Data, JournalEntryFactory, JournalEntryRepository> {

    public ConsequenceJournalEntryConfiguration(Class<JournalEntry> storableClass,
            StorableServiceFactory<JournalEntryFactory, JournalEntryRepository> serviceFactory) {
        super(storableClass, serviceFactory);
    }

    public ConsequenceJournalEntryConfiguration(Class<JournalEntry> storableClass, Class<JournalEntryFactory> factoryClass,
            Class<JournalEntryRepository> repositoryClass) {
        super(storableClass, factoryClass, repositoryClass);
    }

}
