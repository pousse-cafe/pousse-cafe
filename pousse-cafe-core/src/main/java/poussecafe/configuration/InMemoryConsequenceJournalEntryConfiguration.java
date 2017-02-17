package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.journal.InMemoryJournalEntryDataAccess;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryRepository;

public class InMemoryConsequenceJournalEntryConfiguration extends ConsequenceJournalEntryConfiguration {

    public InMemoryConsequenceJournalEntryConfiguration() {
        super(JournalEntry.class, JournalEntryFactory.class, JournalEntryRepository.class);
        setDataFactory(new InMemoryDataFactory<>(JournalEntry.Data.class));
        setDataAccess(new InMemoryJournalEntryDataAccess());
    }

}
