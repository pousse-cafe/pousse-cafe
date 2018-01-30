package poussecafe.journal;

import poussecafe.storable.IdentifiedStorableFactory;

public class JournalEntryFactory extends IdentifiedStorableFactory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    public JournalEntry buildEntryForSentMessage(JournalEntryKey key, SerializedMessage serializedMessage) {
        JournalEntry entry = newStorableWithKey(key);
        entry.setSerializedMessage(serializedMessage);
        entry.setInitialStatus(JournalEntryStatus.IN_PROGRESS);
        return entry;
    }

}
