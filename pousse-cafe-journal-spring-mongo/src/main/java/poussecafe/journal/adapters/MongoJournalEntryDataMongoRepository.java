package poussecafe.journal.adapters;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.adapters.SerializableJournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;

public interface MongoJournalEntryDataMongoRepository extends MongoRepository<MongoJournalEntryData, SerializableJournalEntryKey> {

    List<MongoJournalEntryData> findByMessageId(String messageId);

    List<MongoJournalEntryData> findByStatus(JournalEntryStatus status);

}
