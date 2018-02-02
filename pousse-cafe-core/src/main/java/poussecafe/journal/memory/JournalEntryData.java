package poussecafe.journal.memory;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryKey;
import poussecafe.journal.JournalEntryLog;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.journal.Logs;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.GenericType;
import poussecafe.storage.memory.InlineProperty;

public class JournalEntryData implements JournalEntry.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<Logs> logs() {
        return new ConvertingProperty<ArrayList<JournalEntryLog>, Logs>(logs) {
            @Override
            protected Logs convertFrom(ArrayList<JournalEntryLog> from) {
                return new Logs(from);
            }

            @Override
            protected ArrayList<JournalEntryLog> convertTo(Logs to) {
                return new ArrayList<>(to.asList());
            }
        };
    }

    private InlineProperty<ArrayList<JournalEntryLog>> logs = new InlineProperty<>(new GenericType<ArrayList<JournalEntryLog>>() {}.getRawType());

    @Override
    public void setStatus(JournalEntryStatus status) {
        this.status.set(status);
    }

    private InlineProperty<JournalEntryStatus> status = new InlineProperty<>(JournalEntryStatus.class);

    @Override
    public JournalEntryStatus getStatus() {
        return status.get();
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId.set(messageId);
    }

    private InlineProperty<String> messageId = new InlineProperty<>(String.class);

    @Override
    public String getMessageId() {
        return messageId.get();
    }

    @Override
    public void setMessageData(String messageData) {
        this.messageData.set(messageData);
    }

    private InlineProperty<String> messageData = new InlineProperty<>(String.class);

    @Override
    public String getMessageData() {
        return messageData.get();
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType.set(messageType);
    }

    private InlineProperty<String> messageType = new InlineProperty<>(String.class);

    @Override
    public String getMessageType() {
        return messageType.get();
    }

    @Override
    public Property<JournalEntryKey> key() {
        return new ConvertingProperty<SerializableJournalEntryKey, JournalEntryKey>(key) {
            @Override
            protected JournalEntryKey convertFrom(SerializableJournalEntryKey from) {
                return from.toJournalEntryKey();
            }

            @Override
            protected SerializableJournalEntryKey convertTo(JournalEntryKey to) {
                return new SerializableJournalEntryKey(to);
            }
        };
    }

    private InlineProperty<SerializableJournalEntryKey> key = new InlineProperty<>(SerializableJournalEntryKey.class);

}
