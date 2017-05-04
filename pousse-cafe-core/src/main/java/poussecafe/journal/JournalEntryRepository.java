package poussecafe.journal;

import java.util.List;
import poussecafe.journal.JournalEntry.Data;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.instanceOf;

public class JournalEntryRepository
extends StorableRepository<JournalEntry, JournalEntryKey, JournalEntry.Data> {

    @Override
    protected JournalEntry newStorable() {
        return new JournalEntry();
    }

    public List<JournalEntry> findByMessageId(String messageId) {
        return newStorablesWithData(dataAccess().findByMessageId(messageId));
    }

    @Override
    public void setDataAccess(StorableDataAccess<JournalEntryKey, Data> dataAccess) {
        checkThat(
                value(dataAccess).verifies(instanceOf(JournalEntryDataAccess.class)).because("Data access must be specific"));
        super.setDataAccess(dataAccess);
    }

    protected JournalEntryDataAccess dataAccess() {
        return (JournalEntryDataAccess) dataAccess;
    }

    public List<JournalEntry> findByStatus(JournalEntryStatus status) {
        return newStorablesWithData(dataAccess().findByStatus(status));
    }

}
