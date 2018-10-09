package poussecafe.journal.data;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryStatus;
import poussecafe.journal.domain.Logs;
import poussecafe.property.Property;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("serial")
public class JournalEntryData implements JournalEntry.Data, Serializable {

    @Override
    public Property<Logs> logs() {
        return new Property<Logs>() {
            @Override
            public Logs get() {
                return new Logs(new ArrayList<>(logs.stream().map(JournalEntryLogData::toLog).collect(toList())));
            }

            @Override
            public void set(Logs value) {
                logs.clear();
                logs.addAll(value.asList().stream().map(JournalEntryLogData::new).collect(toList()));
            }
        };
    }

    private ArrayList<JournalEntryLogData> logs = new ArrayList<>();

    @Override
    public void setStatus(JournalEntryStatus status) {
        this.status = status;
    }

    private JournalEntryStatus status;

    @Override
    public JournalEntryStatus getStatus() {
        return status;
    }

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
    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    private String messageData;

    @Override
    public String getMessageData() {
        return messageData;
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
    public Property<JournalEntryKey> key() {
        return new Property<JournalEntryKey>() {
            @Override
            public JournalEntryKey get() {
                return new JournalEntryKey(messageId, listenerId);
            }

            @Override
            public void set(JournalEntryKey value) {
                messageId = value.getMessageId();
                listenerId = value.getListenerId();
            }
        };
    }

    private String listenerId;

}
