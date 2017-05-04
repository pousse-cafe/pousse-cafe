package poussecafe.journal;

import poussecafe.messaging.Message;
import poussecafe.storable.StorableFactory;

public class JournalEntryFactory extends StorableFactory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    @Override
    protected JournalEntry newStorable() {
        return new JournalEntry();
    }

    public JournalEntry buildEntryForSentMessage(JournalEntryKey key, Message message) {
        JournalEntry entry = newStorableWithKey(key);
        entry.setMessage(message);
        entry.setInitialStatus(JournalEntryStatus.IN_PROGRESS);
        return entry;
    }

}
