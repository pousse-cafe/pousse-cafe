package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.collection.Multimap;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.journal.JournalEntry.Data;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, Data> implements JournalEntryDataAccess {

    private Multimap<String, Data> messageIdIndex;

    private Multimap<JournalEntryStatus, Data> statusIndex;

    public InMemoryJournalEntryDataAccess() {
        super(JournalEntry.Data.class);
        messageIdIndex = new Multimap<>();
        statusIndex = new Multimap<>();
    }

    @Override
    public synchronized void addData(Data data) {
        super.addData(data);
        addToIndex(data);
    }

    protected void addToIndex(Data data) {
        messageIdIndex.put(data.getMessage().getId(), data);
        statusIndex.put(data.getStatus(), data);
    }

    @Override
    public synchronized void updateData(Data data) {
        super.updateData(data);
        removeFromIndex(data);
        addToIndex(data);
    }

    private void removeFromIndex(Data data) {
        messageIdIndex.remove(data.getMessage().getId(), data);
        statusIndex.remove(data.getStatus(), data);
    }

    @Override
    public synchronized void deleteData(JournalEntryKey key) {
        Data data = findData(key);
        super.deleteData(key);
        if (data != null) {
            removeFromIndex(data);
        }
    }

    @Override
    public List<Data> findByMessageId(String messageId) {
        return new ArrayList<>(messageIdIndex.get(messageId));
    }

    @Override
    public List<Data> findByStatus(JournalEntryStatus status) {
        return new ArrayList<>(statusIndex.get(status));
    }

}
