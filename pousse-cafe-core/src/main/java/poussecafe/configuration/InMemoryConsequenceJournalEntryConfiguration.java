package poussecafe.configuration;

import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.journal.Entry;
import poussecafe.journal.Entry.Data;
import poussecafe.journal.EntryFactory;
import poussecafe.journal.EntryKey;
import poussecafe.journal.EntryRepository;
import poussecafe.journal.InMemoryEntryDataAccess;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

public class InMemoryConsequenceJournalEntryConfiguration extends ConsequenceJournalEntryConfiguration {

    public InMemoryConsequenceJournalEntryConfiguration() {
        super(Entry.class, EntryFactory.class, EntryRepository.class);
    }

    @Override
    protected StorableDataFactory<Data> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(Entry.Data.class);
    }

    @Override
    protected StorableDataAccess<EntryKey, Data> dataAccess() {
        return new InMemoryEntryDataAccess();
    }

}
