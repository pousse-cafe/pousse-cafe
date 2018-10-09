package poussecafe.journal.domain;

import poussecafe.domain.Factory;
import poussecafe.journal.data.SerializedMessage;

public class JournalEntryFactory extends Factory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    public JournalEntry buildEntryForSentMessage(JournalEntryKey key, SerializedMessage serializedMessage) {
        JournalEntry entry = newAggregateWithKey(key);
        entry.setSerializedMessage(serializedMessage);
        entry.setInitialStatus(JournalEntryStatus.IN_PROGRESS);
        return entry;
    }

}
