package poussecafe.spring.mongo.context;

import java.util.Set;
import poussecafe.domain.EntityImplementation;
import poussecafe.journal.JournalBoundedContext;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.spring.mongo.journal.JournalEntryData;
import poussecafe.spring.mongo.journal.JournalEntryDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

public class MongoJournalBoundedContext extends JournalBoundedContext {

    @Override
    protected void loadImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(JournalEntry.class)
                .withDataFactory(JournalEntryData::new)
                .withDataAccessFactory(JournalEntryDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
    }

}
