package poussecafe.journal.data.memory;

import java.util.Set;
import poussecafe.domain.EntityImplementation;
import poussecafe.journal.JournalBoundedContext;
import poussecafe.journal.data.JournalEntryData;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.storage.memory.InMemoryStorage;

public class MemoryJournalBoundedContext extends JournalBoundedContext {

    @Override
    protected void loadImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(JournalEntry.class)
                .withDataFactory(JournalEntryData::new)
                .withDataAccessFactory(InMemoryJournalEntryDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

}
