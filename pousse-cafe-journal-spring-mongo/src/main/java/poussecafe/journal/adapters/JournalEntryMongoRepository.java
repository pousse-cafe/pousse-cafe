package poussecafe.journal.adapters;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.data.SerializableJournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;

public interface JournalEntryMongoRepository extends MongoRepository<JournalEntryData, SerializableJournalEntryKey> {

    List<JournalEntryData> findByMessageId(String messageId);

    List<JournalEntryData> findByStatus(JournalEntryStatus status);

}
