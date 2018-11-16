package poussecafe.journal.domain;

import poussecafe.domain.Factory;

public class JournalEntryFactory extends Factory<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    public JournalEntry buildEntryForSentMessage(JournalEntryKey key, String serializedMessage) {
        JournalEntry entry = newAggregateWithKey(key);
        entry.setSerializedMessage(serializedMessage);
        entry.setInitialStatus(JournalEntryStatus.IN_PROGRESS);
        return entry;
    }

}
