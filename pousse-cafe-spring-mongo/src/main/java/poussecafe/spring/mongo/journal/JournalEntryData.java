package poussecafe.spring.mongo.journal;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.journal.Logs;
import poussecafe.journal.memory.SerializableJournalEntryKey;
import poussecafe.storable.BaseProperty;
import poussecafe.storable.Property;

public class JournalEntryData implements JournalEntry.Data {

    @Override
    public Property<JournalEntryKey> key() {
        return new BaseProperty<JournalEntryKey>() {
            @Override
            protected JournalEntryKey getValue() {
                return key.toJournalEntryKey();
            }

            @Override
            protected void setValue(JournalEntryKey value) {
                key = new SerializableJournalEntryKey(value);
                messageId = value.getMessageId();
            }
        };
    }

    @Id
    private SerializableJournalEntryKey key;

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private String messageId;

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    private String messageType;

    @Override
    public String getMessageType() {
        return messageType;
    }

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
        return new BaseProperty<Logs>() {
            @Override
            protected Logs getValue() {
                if(logs == null) {
                    return new Logs(new ArrayList<>());
                } else {
                    return logs;
                }
            }

            @Override
            protected void setValue(Logs value) {
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
