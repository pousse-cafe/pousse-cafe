package poussecafe.messaging;

import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class FailedConsumption extends DomainEvent {

    public FailedConsumption(String listenerId, Message consumedMessage, String error) {
        setListenerId(listenerId);
        setConsumedMessage(consumedMessage);
        setError(error);
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
        return "FailedConsumption [listenerId=" + listenerId + ", consumedMessage=" + consumedMessage  + ", error=" + error + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((consumedMessage == null) ? 0 : consumedMessage.hashCode());
        result = prime * result + ((listenerId == null) ? 0 : listenerId.hashCode());
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FailedConsumption other = (FailedConsumption) obj;
        if (consumedMessage == null) {
            if (other.consumedMessage != null) {
                return false;
            }
        } else if (!consumedMessage.equals(other.consumedMessage)) {
            return false;
        }
        if (listenerId == null) {
            if (other.listenerId != null) {
                return false;
            }
        } else if (!listenerId.equals(other.listenerId)) {
            return false;
        } else if (!error.equals(other.error)) {
            return false;
        }
        return true;
    }
}
