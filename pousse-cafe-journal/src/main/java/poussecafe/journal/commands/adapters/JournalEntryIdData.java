package poussecafe.journal.commands.adapters;

import java.io.Serializable;
import poussecafe.journal.domain.JournalEntryId;

@SuppressWarnings("serial")
public class JournalEntryIdData implements Serializable {

    public static JournalEntryIdData adapt(JournalEntryId journalEntryId) {
        JournalEntryIdData data = new JournalEntryIdData();
        data.consumptionId = journalEntryId.getConsumptionId();
        data.listenerId = journalEntryId.getListenerId();
        return data;
    }

    private String consumptionId;

    private String listenerId;

    public JournalEntryId adapt() {
        return new JournalEntryId(consumptionId, listenerId);
    }
}
