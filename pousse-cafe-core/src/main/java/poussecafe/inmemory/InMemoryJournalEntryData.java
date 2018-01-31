package poussecafe.inmemory;

import java.util.List;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryLog;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.storable.Property;

public class InMemoryJournalEntryData implements JournalEntry.Data {

    @Override
    public List<JournalEntryLog> getLogs() {
        return logs.get();
    }

    private InlineProperty<List<JournalEntryLog>> logs = new InlineProperty<>(
            new GenericType<List<JournalEntryLog>>().getRawType());

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
    public Property<String> listenerId() {
        return listenerId;
    }

    private InlineProperty<String> listenerId = new InlineProperty<>(String.class);

}
