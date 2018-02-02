package poussecafe.journal;

import java.util.List;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface JournalEntryDataAccess<D extends JournalEntry.Data> extends IdentifiedStorableDataAccess<JournalEntryKey, D> {

    List<D> findByMessageId(String messageId);

    List<D> findByStatus(JournalEntryStatus status);
}
