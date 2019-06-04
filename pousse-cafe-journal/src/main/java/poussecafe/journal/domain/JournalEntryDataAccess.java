package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface JournalEntryDataAccess<D extends JournalEntry.Attributes> extends EntityDataAccess<JournalEntryId, D> {

    List<D> findByStatus(ConsumptionStatus status);
}
