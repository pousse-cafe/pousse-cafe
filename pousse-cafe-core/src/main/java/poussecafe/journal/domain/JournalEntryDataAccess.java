package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface JournalEntryDataAccess<D extends JournalEntry.Data> extends EntityDataAccess<JournalEntryKey, D> {

    List<D> findByMessageId(String messageId);

    List<D> findByStatus(JournalEntryStatus status);
}
