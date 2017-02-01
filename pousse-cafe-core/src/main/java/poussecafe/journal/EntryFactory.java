package poussecafe.journal;

import poussecafe.consequence.Consequence;
import poussecafe.storable.StorableFactory;

public class EntryFactory
extends StorableFactory<EntryKey, Entry, Entry.Data> {

    @Override
    protected Entry newStorable() {
        return new Entry();
    }

    public Entry buildEntryForEmittedConsequence(EntryKey key, Consequence consequence) {
        Entry entry = newStorableWithKey(key);
        entry.setConsequence(consequence);
        return entry;
    }

}
