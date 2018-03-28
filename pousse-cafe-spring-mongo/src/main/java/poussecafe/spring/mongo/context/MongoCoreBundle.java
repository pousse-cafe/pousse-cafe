package poussecafe.spring.mongo.context;

import java.util.Set;
import poussecafe.context.CoreBundle;
import poussecafe.journal.JournalEntry;
import poussecafe.spring.mongo.journal.JournalEntryData;
import poussecafe.spring.mongo.journal.JournalEntryDataAccess;
import poussecafe.spring.mongo.storage.MongoDbStorage;
import poussecafe.storable.StorableImplementation;

public class MongoCoreBundle extends CoreBundle {

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(JournalEntry.class)
                .withDataFactory(JournalEntryData::new)
                .withDataAccessFactory(JournalEntryDataAccess::new)
                .withStorage(MongoDbStorage.instance())
                .build());
    }

}
