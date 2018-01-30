package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.collection.Multimap;
import poussecafe.inmemory.InMemoryDataAccess;
import poussecafe.journal.JournalEntry.Data;

import static java.util.stream.Collectors.toList;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<Data> implements JournalEntryDataAccess {

    private Multimap<String, Object> messageIdIndex;

    private Multimap<JournalEntryStatus, Object> statusIndex;

    public InMemoryJournalEntryDataAccess() {
        messageIdIndex = new Multimap<>();
        statusIndex = new Multimap<>();
    }

    @Override
    protected JournalEntryKey extractKey(Data data) {
        return new JournalEntryKey(data.getMessageId(), data.listenerId().get());
    }

    @Override
    public synchronized void addData(Data data) {
        super.addData(data);
        addToIndex(data);
    }

    protected void addToIndex(Data data) {
        JournalEntryKey key = extractKey(data);
        messageIdIndex.put(data.getMessageId(), key);
        statusIndex.put(data.getStatus(), key);
    }

    @Override
    public synchronized void updateData(Data data) {
        super.updateData(data);
        removeFromIndex(data);
        addToIndex(data);
    }

    private void removeFromIndex(Data data) {
        JournalEntryKey key = extractKey(data);
        messageIdIndex.remove(data.getMessageId(), key);
        statusIndex.remove(data.getStatus(), key);
    }

    @Override
    public synchronized void deleteData(Object key) {
        Data data = findData(key);
        super.deleteData(key);
        if (data != null) {
            removeFromIndex(data);
        }
    }

    @Override
    public List<Data> findByMessageId(String messageId) {
        return new ArrayList<>(messageIdIndex.get(messageId).stream().map(this::findData).collect(toList()));
    }

    @Override
    public List<Data> findByStatus(JournalEntryStatus status) {
        return new ArrayList<>(statusIndex.get(status).stream().map(this::findData).collect(toList()));
    }

}
