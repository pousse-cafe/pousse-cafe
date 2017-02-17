package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.collection.Multimap;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.journal.JournalEntry.Data;

public class InMemoryJournalEntryDataAccess extends InMemoryDataAccess<JournalEntryKey, Data> implements JournalEntryDataAccess {

    private Multimap<String, Data> consequenceIdIndex;

    private Multimap<JournalEntryStatus, Data> statusIndex;

    public InMemoryJournalEntryDataAccess() {
        super(JournalEntry.Data.class);
        consequenceIdIndex = new Multimap<>();
        statusIndex = new Multimap<>();
    }

    @Override
    public synchronized void addData(Data data) {
        super.addData(data);
        addToIndex(data);
    }

    protected void addToIndex(Data data) {
        consequenceIdIndex.put(data.getConsequence().getId(), data);
        statusIndex.put(data.getStatus(), data);
    }

    @Override
    public synchronized void updateData(Data data) {
        super.updateData(data);
        removeFromIndex(data);
        addToIndex(data);
    }

    private void removeFromIndex(Data data) {
        consequenceIdIndex.remove(data.getConsequence().getId(), data);
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
    public List<Data> findByConsequenceId(String consequenceId) {
        return new ArrayList<>(consequenceIdIndex.get(consequenceId));
    }

    @Override
    public List<Data> findByStatus(JournalEntryStatus status) {
        return new ArrayList<>(statusIndex.get(status));
    }

}
