package poussecafe.journal.memory;

import java.util.List;
import poussecafe.journal.JournalEntryDataAccess;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.Arrays.asList;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, InMemoryJournalEntryData> implements JournalEntryDataAccess<InMemoryJournalEntryData> {

    @Override
    protected List<Object> extractIndexedData(InMemoryJournalEntryData data) {
        return asList(data.getMessageId(), data.getStatus());
    }

    @Override
    public List<InMemoryJournalEntryData> findByMessageId(String messageId) {
        return findBy(messageId);
    }

    @Override
    public List<InMemoryJournalEntryData> findByStatus(JournalEntryStatus status) {
        return findBy(status);
    }

}
