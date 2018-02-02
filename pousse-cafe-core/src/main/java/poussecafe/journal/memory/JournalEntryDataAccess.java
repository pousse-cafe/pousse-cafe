package poussecafe.journal.memory;

import java.util.List;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.Arrays.asList;

public class JournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, JournalEntryData> implements poussecafe.journal.JournalEntryDataAccess<JournalEntryData> {

    @Override
    protected List<Object> extractIndexedData(JournalEntryData data) {
        return asList(data.getMessageId(), data.getStatus());
    }

    @Override
    public List<JournalEntryData> findByMessageId(String messageId) {
        return findBy(messageId);
    }

    @Override
    public List<JournalEntryData> findByStatus(JournalEntryStatus status) {
        return findBy(status);
    }

}
