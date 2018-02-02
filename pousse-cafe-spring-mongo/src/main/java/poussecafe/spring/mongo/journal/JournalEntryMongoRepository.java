package poussecafe.spring.mongo.journal;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.journal.memory.SerializableJournalEntryKey;

public interface JournalEntryMongoRepository extends MongoRepository<JournalEntryData, SerializableJournalEntryKey> {

    List<JournalEntryData> findByMessageId(String messageId);

    List<JournalEntryData> findByStatus(JournalEntryStatus status);

}
