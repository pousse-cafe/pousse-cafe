package poussecafe.journal;

import java.util.List;
import poussecafe.storable.IdentifiedStorableRepository;

public class JournalEntryRepository extends IdentifiedStorableRepository<JournalEntry, JournalEntryKey, JournalEntry.Data> {

    public List<JournalEntry> findByMessageId(String messageId) {
        return newStorablesWithData(dataAccess().findByMessageId(messageId));
    }

    protected JournalEntryDataAccess dataAccess() {
        return (JournalEntryDataAccess) dataAccess;
    }

    public List<JournalEntry> findByStatus(JournalEntryStatus status) {
        return newStorablesWithData(dataAccess().findByStatus(status));
    }

}
