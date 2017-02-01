package poussecafe.configuration;

import poussecafe.journal.Entry;
import poussecafe.journal.EntryFactory;
import poussecafe.journal.EntryKey;
import poussecafe.journal.EntryRepository;

public abstract class ConsequenceJournalEntryConfiguration
extends StorableConfiguration<EntryKey, Entry, Entry.Data, EntryFactory, EntryRepository> {

    public ConsequenceJournalEntryConfiguration(Class<Entry> storableClass,
            StorableServiceFactory<EntryFactory, EntryRepository> serviceFactory) {
        super(storableClass, serviceFactory);
    }

    public ConsequenceJournalEntryConfiguration(Class<Entry> storableClass, Class<EntryFactory> factoryClass,
            Class<EntryRepository> repositoryClass) {
        super(storableClass, factoryClass, repositoryClass);
    }

}
