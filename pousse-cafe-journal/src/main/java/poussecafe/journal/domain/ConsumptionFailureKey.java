package poussecafe.journal.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class ConsumptionFailureKey {

    public ConsumptionFailureKey(Message message, String consumptionId) {
        this.message = message;
        this.consumptionId = consumptionId;
    }

    private Message message;

    public Message message() {
        return message;
    }

    private String consumptionId;

    public String consumptionId() {
        return consumptionId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(message)
                .append(consumptionId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(message, other.message)
                .append(consumptionId, other.consumptionId)
                .build());
    }
}
