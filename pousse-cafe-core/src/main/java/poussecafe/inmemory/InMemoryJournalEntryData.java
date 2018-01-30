package poussecafe.inmemory;

import java.util.List;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryLog;
import poussecafe.journal.JournalEntryStatus;
import poussecafe.storable.Property;

public class InMemoryJournalEntryData extends GenericInMemoryData implements JournalEntry.Data {

    @SuppressWarnings("unchecked")
    @Override
    public List<JournalEntryLog> getLogs() {
        return property(List.class, "logs").get();
    }

    @Override
    public void setStatus(JournalEntryStatus status) {
        setProperty("status", status);
    }

    @Override
    public JournalEntryStatus getStatus() {
        return property(JournalEntryStatus.class, "status").get();
    }

    @Override
    public void setMessageId(String messageId) {
        setProperty("messageId", messageId);
    }

    @Override
    public String getMessageId() {
        return property(String.class, "messageId").get();
    }

    @Override
    public void setMessageData(String messageData) {
        setProperty("messageData", messageData);
    }

    @Override
    public String getMessageData() {
        return property(String.class, "messageData").get();
    }

    @Override
    public void setMessageType(String messageType) {
        setProperty("messageType", messageType);
    }

    @Override
    public String getMessageType() {
        return property(String.class, "messageType").get();
    }

    @Override
    public Property<String> listenerId() {
        return listenerId;
    }

    private InlineProperty<String> listenerId = new InlineProperty<>(String.class);

}
