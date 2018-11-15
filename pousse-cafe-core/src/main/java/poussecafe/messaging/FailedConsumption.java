package poussecafe.messaging;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;
import static poussecafe.util.ReferenceEquals.referenceEquals;

public class FailedConsumption implements DomainEvent {

    public FailedConsumption(String consumptionId, String listenerId, Message consumedMessage, String error) {
        setConsumptionId(consumptionId);
        setListenerId(listenerId);
        setConsumedMessage(consumedMessage);
        setError(error);
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

    private void setError(String error) {
        checkThat(value(error).notNull().because("Listener ID cannot be null"));
        this.error = error;
    }

    private String error;

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(consumptionId)
                .append(consumedMessage)
                .append(listenerId)
                .append(error)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(consumptionId)
                .append(consumedMessage)
                .append(listenerId)
                .append(error)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(consumptionId, other.consumptionId)
                .append(consumedMessage, other.consumedMessage)
                .append(listenerId, other.listenerId)
                .append(error, other.error)
                .build());
    }
}
