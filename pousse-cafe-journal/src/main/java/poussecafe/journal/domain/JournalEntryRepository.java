package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Repository;

public class JournalEntryRepository extends Repository<JournalEntry, JournalEntryId, JournalEntry.Attributes> {

    @Override
    public JournalEntryDataAccess<JournalEntry.Attributes> dataAccess() {
        return (JournalEntryDataAccess<JournalEntry.Attributes>) super.dataAccess();
    }

    public List<JournalEntry> findByStatus(ConsumptionStatus status) {
        return wrap(dataAccess().findByStatus(status));
    }

}
