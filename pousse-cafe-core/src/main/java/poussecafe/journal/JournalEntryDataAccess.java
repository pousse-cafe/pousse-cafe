package poussecafe.journal;

import java.util.List;
import poussecafe.storable.StorableDataAccess;

public interface JournalEntryDataAccess extends StorableDataAccess<JournalEntryKey, JournalEntry.Data> {

    List<JournalEntry.Data> findByConsequenceId(String consequenceId);

    List<JournalEntry.Data> findByStatus(JournalEntryStatus status);
}
