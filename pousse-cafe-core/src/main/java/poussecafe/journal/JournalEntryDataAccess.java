package poussecafe.journal;

import java.util.List;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface JournalEntryDataAccess extends IdentifiedStorableDataAccess<JournalEntry.Data> {

    List<JournalEntry.Data> findByMessageId(String messageId);

    List<JournalEntry.Data> findByStatus(JournalEntryStatus status);
}
