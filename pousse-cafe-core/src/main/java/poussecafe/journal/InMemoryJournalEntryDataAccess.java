package poussecafe.journal;

import java.util.List;
import poussecafe.inmemory.InMemoryDataAccess;
import poussecafe.journal.JournalEntry.Data;

import static java.util.Arrays.asList;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, Data> implements JournalEntryDataAccess {

    @Override
    protected List<Object> extractIndexedData(Data data) {
        return asList(data.getMessageId(), data.getStatus());
    }

    @Override
    public List<Data> findByMessageId(String messageId) {
        return findBy(messageId);
    }

    @Override
    public List<Data> findByStatus(JournalEntryStatus status) {
        return findBy(status);
    }

}
