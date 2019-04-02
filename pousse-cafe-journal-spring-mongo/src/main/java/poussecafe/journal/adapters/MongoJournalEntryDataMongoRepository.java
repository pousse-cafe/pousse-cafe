package poussecafe.journal.adapters;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.journal.adapters.SerializableJournalEntryId;
import poussecafe.journal.domain.ConsumptionStatus;

public interface MongoJournalEntryDataMongoRepository extends MongoRepository<MongoJournalEntryData, SerializableJournalEntryId> {

    List<MongoJournalEntryData> findByMessageId(String messageId);

    List<MongoJournalEntryData> findByStatus(ConsumptionStatus status);

}
