package poussecafe.journal.domain;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class JournalEntryKey {

    private String messageId;

    private String listenerId;

    public JournalEntryKey(String messageId, String listenerId) {
        setMessageId(messageId);
        setListenerId(listenerId);
    }

    public String getMessageId() {
        return messageId;
    }

    private void setMessageId(String messageId) {
        checkThat(value(messageId).verifies(not(emptyOrNullString())).because("Message ID cannot be null"));
        this.messageId = messageId;
    }

    public String getListenerId() {
        return listenerId;
    }

    private void setListenerId(String listenerId) {
        checkThat(value(listenerId).verifies(not(emptyOrNullString())).because("Listener ID cannot be null"));
        this.listenerId = listenerId;
    }

    @Override
    public int hashCode() {
        return 31 * messageId.hashCode() + 31 * listenerId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JournalEntryKey other = (JournalEntryKey) obj;
        if (!messageId.equals(other.messageId)) {
            return false;
        }
        if (!listenerId.equals(other.listenerId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntryKey [messageId=" + messageId + ", listenerId=" + listenerId + "]";
    }

}
