package poussecafe.journal.data.memory;

import java.util.List;
import poussecafe.journal.data.JournalEntryData;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.Arrays.asList;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, JournalEntryData> implements poussecafe.journal.domain.JournalEntryDataAccess<JournalEntryData> {

    @Override
    protected List<Object> extractIndexedData(JournalEntryData data) {
        return asList(data.key().get().getConsumptionId(), data.getStatus());
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
