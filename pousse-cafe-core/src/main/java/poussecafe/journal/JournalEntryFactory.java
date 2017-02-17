package poussecafe.journal;

import poussecafe.consequence.Consequence;
import poussecafe.storable.StorableFactory;

public class JournalEntryFactory extends StorableFactory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    @Override
    protected JournalEntry newStorable() {
        return new JournalEntry();
    }

    public JournalEntry buildEntryForEmittedConsequence(JournalEntryKey key, Consequence consequence) {
        JournalEntry entry = newStorableWithKey(key);
        entry.setConsequence(consequence);
        entry.setInitialStatus(JournalEntryStatus.IN_PROGRESS);
        return entry;
    }

}
