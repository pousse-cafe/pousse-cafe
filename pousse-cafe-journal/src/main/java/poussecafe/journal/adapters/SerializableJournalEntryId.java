package poussecafe.journal.adapters;

import java.io.Serializable;
import poussecafe.journal.domain.JournalEntryId;

@SuppressWarnings("serial")
public class SerializableJournalEntryId implements Serializable {

    public SerializableJournalEntryId(JournalEntryId to) {
        messageId = to.getConsumptionId();
        listenerId = to.getListenerId();
    }

    private String messageId;

    private String listenerId;

    public JournalEntryId toJournalEntryId() {
        return new JournalEntryId(messageId, listenerId);
    }

}
