package poussecafe.journal.memory;

import java.io.Serializable;
import poussecafe.journal.JournalEntryKey;

@SuppressWarnings("serial")
public class SerializableJournalEntryKey implements Serializable {

    public SerializableJournalEntryKey(JournalEntryKey to) {
        messageId = to.getMessageId();
        listenerId = to.getListenerId();
    }

    private String messageId;

    private String listenerId;

    public JournalEntryKey toJournalEntryKey() {
        return new JournalEntryKey(messageId, listenerId);
    }

}
