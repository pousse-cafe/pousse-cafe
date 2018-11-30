package poussecafe.journal.adapters;

import java.io.Serializable;
import poussecafe.journal.domain.JournalEntryKey;

@SuppressWarnings("serial")
public class SerializableJournalEntryKey implements Serializable {

    public SerializableJournalEntryKey(JournalEntryKey to) {
        messageId = to.getConsumptionId();
        listenerId = to.getListenerId();
    }

    private String messageId;

    private String listenerId;

    public JournalEntryKey toJournalEntryKey() {
        return new JournalEntryKey(messageId, listenerId);
    }

}
