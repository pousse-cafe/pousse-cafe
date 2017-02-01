package poussecafe.journal;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class EntryKey {

    private String consequenceId;

    private String listenerId;

    public EntryKey(String consequenceId, String listenerId) {
        setConsequenceId(consequenceId);
        setListenerId(listenerId);
    }

    public String getConsequenceId() {
        return consequenceId;
    }

    private void setConsequenceId(String consequenceId) {
        checkThat(value(consequenceId).verifies(not(emptyOrNullString())).because("Consequence ID cannot be null"));
        this.consequenceId = consequenceId;
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
        return 31 * consequenceId.hashCode() + 31 * listenerId.hashCode();
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
        EntryKey other = (EntryKey) obj;
        if (!consequenceId.equals(other.consequenceId)) {
            return false;
        }
        if (!listenerId.equals(other.listenerId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntryKey [consequenceId=" + consequenceId + ", listenerId=" + listenerId + "]";
    }

}
