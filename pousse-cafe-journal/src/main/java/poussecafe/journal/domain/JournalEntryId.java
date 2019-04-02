package poussecafe.journal.domain;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class JournalEntryId {

    private String consumptionId;

    private String listenerId;

    public JournalEntryId(String consumptionId, String listenerId) {
        setConsumptionId(consumptionId);
        setListenerId(listenerId);
    }

    public String getConsumptionId() {
        return consumptionId;
    }

    private void setConsumptionId(String consumptionId) {
        Objects.requireNonNull(consumptionId);
        this.consumptionId = consumptionId;
    }

    public String getListenerId() {
        return listenerId;
    }

    private void setListenerId(String listenerId) {
        Objects.requireNonNull(listenerId);
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
