package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.collection.Multimap;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.journal.Entry.Data;

public class InMemoryEntryDataAccess extends InMemoryDataAccess<EntryKey, Data> implements EntryDataAccess {

    private Multimap<String, Data> consequenceIdIndex;

    public InMemoryEntryDataAccess() {
        super(Entry.Data.class);
        consequenceIdIndex = new Multimap<>();
    }

    @Override
    public synchronized void addData(Data data) {
        super.addData(data);
        addToIndex(data);
    }

    protected void addToIndex(Data data) {
        consequenceIdIndex.put(data.getConsequence().getId(), data);
    }

    @Override
    public synchronized void updateData(Data data) {
        super.updateData(data);
        removeFromIndex(data);
        addToIndex(data);
    }

    private void removeFromIndex(Data data) {
        consequenceIdIndex.remove(data.getConsequence().getId(), data);
    }

    @Override
    public synchronized void deleteData(EntryKey key) {
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

}
