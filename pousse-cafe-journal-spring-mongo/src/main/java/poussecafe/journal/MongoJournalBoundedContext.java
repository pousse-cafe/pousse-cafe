package poussecafe.journal;

import java.util.Set;
import poussecafe.domain.EntityImplementation;
import poussecafe.journal.adapters.JournalEntryData;
import poussecafe.journal.adapters.JournalEntryDataAccess;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

public class MongoJournalBoundedContext extends JournalBoundedContext {

    @Override
    protected void loadEntityImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(JournalEntry.class)
                .withDataFactory(JournalEntryData::new)
                .withDataAccessFactory(JournalEntryDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
    }

}
