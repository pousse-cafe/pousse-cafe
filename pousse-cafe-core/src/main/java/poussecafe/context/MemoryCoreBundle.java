package poussecafe.context;

import java.util.Set;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.memory.JournalEntryData;
import poussecafe.journal.memory.JournalEntryDataAccess;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.memory.InMemoryStorage;

public class MemoryCoreBundle extends CoreBundle {

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(JournalEntry.class)
                .withDataFactory(JournalEntryData::new)
                .withDataAccessFactory(JournalEntryDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

}
