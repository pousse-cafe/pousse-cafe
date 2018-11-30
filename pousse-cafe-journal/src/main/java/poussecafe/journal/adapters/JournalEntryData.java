package poussecafe.journal.adapters;

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
    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    private String messageData;

    @Override
    public String getMessageData() {
        return messageData;
    }

    @Override
    public Property<JournalEntryKey> key() {
        return new Property<JournalEntryKey>() {
            @Override
            public JournalEntryKey get() {
                return new JournalEntryKey(consumptionId, listenerId);
            }

            @Override
            public void set(JournalEntryKey value) {
                consumptionId = value.getConsumptionId();
                listenerId = value.getListenerId();
            }
        };
    }

    private String consumptionId;

    private String listenerId;

}
