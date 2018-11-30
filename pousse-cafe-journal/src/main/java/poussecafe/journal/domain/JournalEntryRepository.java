package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class JournalEntryRepository extends Repository<JournalEntry, JournalEntryKey, JournalEntry.Data> {

    public List<JournalEntry> findByConsumptionId(String messageId) {
        return newEntitiesWithData(dataAccess().findByMessageId(messageId));
    }

    protected JournalEntryDataAccess<JournalEntry.Data> dataAccess() {
        return (JournalEntryDataAccess<JournalEntry.Data>) dataAccess;
    }

    public List<JournalEntry> findByStatus(ConsumptionStatus status) {
        return newEntitiesWithData(dataAccess().findByStatus(status));
    }

}
