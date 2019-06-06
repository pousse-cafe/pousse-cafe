package poussecafe.journal.adapters;

import java.io.Serializable;
import poussecafe.journal.domain.JournalEntryId;

@SuppressWarnings("serial")
public class SerializableJournalEntryId implements Serializable {

    public static SerializableJournalEntryId adapt(JournalEntryId to) {
        SerializableJournalEntryId data = new SerializableJournalEntryId();
        data.consumptionId = to.getConsumptionId();
        data.listenerId = to.getListenerId();
        return data;
    }

    private String consumptionId;

    private String listenerId;

    public JournalEntryId adapt() {
        return new JournalEntryId(consumptionId, listenerId);
    }

}
