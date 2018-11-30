package poussecafe.journal.adapters;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.adapters.SerializableJournalEntryKey;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.ConsumptionStatus;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;
import poussecafe.storage.DataAccessImplementation;

@DataAccessImplementation(
    aggregateRoot = JournalEntry.class,
    dataImplementation = MongoJournalEntryData.class,
    storageName = SpringMongoDbStorage.NAME
)
public class MongoJournalEntryDataAccess extends MongoDataAccess<JournalEntryKey, MongoJournalEntryData, SerializableJournalEntryKey>
        implements poussecafe.journal.domain.JournalEntryDataAccess<MongoJournalEntryData> {

    @Autowired
    private MongoJournalEntryDataMongoRepository repository;

    @Override
    public List<MongoJournalEntryData> findByMessageId(String messageId) {
        return repository.findByMessageId(messageId);
    }

    @Override
    public List<MongoJournalEntryData> findByStatus(ConsumptionStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    protected SerializableJournalEntryKey convertKey(JournalEntryKey key) {
        return new SerializableJournalEntryKey(key);
    }

    @Override
    protected MongoRepository<MongoJournalEntryData, SerializableJournalEntryKey> mongoRepository() {
        return repository;
    }

}
