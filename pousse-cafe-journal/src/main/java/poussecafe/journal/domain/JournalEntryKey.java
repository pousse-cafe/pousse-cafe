package poussecafe.journal.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;
import static poussecafe.util.ReferenceEquals.referenceEquals;

public class JournalEntryKey {

    private String consumptionId;

    private String listenerId;

    public JournalEntryKey(String consumptionId, String listenerId) {
        setConsumptionId(consumptionId);
        setListenerId(listenerId);
    }

    public String getConsumptionId() {
        return consumptionId;
    }

    private void setConsumptionId(String consumptionId) {
        checkThat(value(consumptionId).verifies(not(emptyOrNullString())).because("Message ID cannot be null"));
        this.consumptionId = consumptionId;
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
        return new HashCodeBuilder()
                .append(consumptionId)
                .append(listenerId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(consumptionId, other.consumptionId)
                .append(listenerId, other.listenerId)
                .build());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(consumptionId)
                .append(listenerId)
                .build();
    }

}
