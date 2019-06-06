package poussecafe.journal.adapters;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryId;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataAccessImplementation(
    aggregateRoot = JournalEntry.class,
    dataImplementation = MongoJournalEntryData.class,
    storageName = SpringMongoDbStorage.NAME
)
public class MongoJournalEntryDataAccess extends MongoDataAccess<JournalEntryId, MongoJournalEntryData, SerializableJournalEntryId>
        implements poussecafe.journal.domain.JournalEntryDataAccess<MongoJournalEntryData> {

    @Autowired
    private MongoJournalEntryDataMongoRepository repository;

    @Override
    public List<MongoJournalEntryData> findByStatus(ConsumptionStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    protected SerializableJournalEntryId convertId(JournalEntryId id) {
        return SerializableJournalEntryId.adapt(id);
    }

    @Override
    protected MongoRepository<MongoJournalEntryData, SerializableJournalEntryId> mongoRepository() {
        return repository;
    }

}
