package poussecafe.spring.mongo.journal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.data.SerializableJournalEntryKey;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class JournalEntryDataAccess extends MongoDataAccess<JournalEntryKey, JournalEntryData, SerializableJournalEntryKey>
        implements poussecafe.journal.domain.JournalEntryDataAccess<JournalEntryData> {

    @Autowired
    private JournalEntryMongoRepository repository;

    @Override
    public List<JournalEntryData> findByMessageId(String messageId) {
        return repository.findByMessageId(messageId);
    }

    @Override
    public List<JournalEntryData> findByStatus(JournalEntryStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    protected SerializableJournalEntryKey convertKey(JournalEntryKey key) {
        return new SerializableJournalEntryKey(key);
    }

    @Override
    protected MongoRepository<JournalEntryData, SerializableJournalEntryKey> mongoRepository() {
        return repository;
    }

}
