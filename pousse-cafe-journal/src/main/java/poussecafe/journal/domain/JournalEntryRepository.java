package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class JournalEntryRepository extends Repository<JournalEntry, JournalEntryKey, JournalEntry.Data> {

    public List<JournalEntry> findByConsumptionId(String messageId) {
        return wrap(dataAccess().findByMessageId(messageId));
    }

    @Override
    public JournalEntryDataAccess<JournalEntry.Data> dataAccess() {
        return (JournalEntryDataAccess<JournalEntry.Data>) super.dataAccess();
    }

    public List<JournalEntry> findByStatus(ConsumptionStatus status) {
        return wrap(dataAccess().findByStatus(status));
    }

}
