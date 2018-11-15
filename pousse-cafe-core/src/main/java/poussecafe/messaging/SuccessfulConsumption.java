package poussecafe.messaging;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;
import static poussecafe.util.ReferenceEquals.referenceEquals;

public class SuccessfulConsumption implements DomainEvent {

    public SuccessfulConsumption(String consumptionId, String listenerId, Message consumedMessage) {
        setConsumptionId(consumptionId);
        setListenerId(listenerId);
        setConsumedMessage(consumedMessage);
    }

    private void setConsumptionId(String consumptionId) {
        checkThatValue(consumptionId).notNull();
        this.consumptionId = consumptionId;
    }

    private String consumptionId;

    public String getConsumptionId() {
        return consumptionId;
    }

    private void setListenerId(String listenerId) {
        checkThat(value(listenerId).notNull().because("Listener ID cannot be null"));
        this.listenerId = listenerId;
    }

    private String listenerId;

    public String getListenerId() {
        return listenerId;
    }

    private void setConsumedMessage(Message consumedMessage) {
        checkThat(value(consumedMessage).notNull().because("Consumed message cannot be null"));
        this.consumedMessage = consumedMessage;
    }

    private Message consumedMessage;

    public Message getConsumedMessage() {
        return consumedMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(consumptionId)
                .append(consumedMessage)
                .append(listenerId)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(consumptionId)
                .append(consumedMessage)
                .append(listenerId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(consumptionId, other.consumptionId)
                .append(consumedMessage, other.consumedMessage)
                .append(listenerId, other.listenerId)
                .build());
    }
}
