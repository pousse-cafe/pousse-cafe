package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.journal.Entry;
import poussecafe.journal.EntryFactory;
import poussecafe.journal.EntryRepository;
import poussecafe.journal.InMemoryEntryDataAccess;

public class InMemoryConsequenceJournalEntryConfiguration extends ConsequenceJournalEntryConfiguration {

    public InMemoryConsequenceJournalEntryConfiguration() {
        super(Entry.class, EntryFactory.class, EntryRepository.class);
        setDataFactory(new InMemoryDataFactory<>(Entry.Data.class));
        setDataAccess(new InMemoryEntryDataAccess());
    }

}
