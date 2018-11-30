package poussecafe.journal.adapters;

import org.springframework.data.annotation.Id;
import poussecafe.journal.adapters.SerializableJournalEntryKey;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;
import poussecafe.journal.domain.Logs;
import poussecafe.property.Property;

public class MongoJournalEntryData implements JournalEntry.Data {

    @Override
    public Property<JournalEntryKey> key() {
        return new Property<JournalEntryKey>() {
            @Override
            public JournalEntryKey get() {
                return key.toJournalEntryKey();
            }

            @Override
            public void set(JournalEntryKey value) {
                key = new SerializableJournalEntryKey(value);
            }
        };
    }

    @Id
    private SerializableJournalEntryKey key;

    @Override
    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    private String messageData;

    @Override
    public String getMessageData() {
        return messageData;
    }

    @Override
    public Property<Logs> logs() {
        return new Property<Logs>() {
            @Override
            public Logs get() {
                return logs;
            }

            @Override
            public void set(Logs value) {
                logs = value;
            }
        };
    }

    private Logs logs;

    @Override
    public void setStatus(JournalEntryStatus status) {
        this.status = status;
    }

    private JournalEntryStatus status;

    @Override
    public JournalEntryStatus getStatus() {
        return status;
    }

}
