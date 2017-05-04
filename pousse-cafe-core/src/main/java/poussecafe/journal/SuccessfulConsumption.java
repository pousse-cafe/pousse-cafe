package poussecafe.journal;

import poussecafe.messaging.Message;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SuccessfulConsumption {

    private Message consumedMessage;

    private ProcessManagerKey createdProcessManagerKey;

    public SuccessfulConsumption(Message consumedMessage) {
        this(consumedMessage, null);
    }

    public SuccessfulConsumption(Message consumedMessage, ProcessManagerKey createdProcessManagerKey) {
        setConsumedMessage(consumedMessage);
        setCreatedProcessManagerKey(createdProcessManagerKey);
    }

    public Message getConsumedMessage() {
        return consumedMessage;
    }

    private void setConsumedMessage(Message consumedMessage) {
        checkThat(value(consumedMessage).notNull().because("Consumed message cannot be null"));
        this.consumedMessage = consumedMessage;
    }

    public ProcessManagerKey getCreatedProcessManagerKey() {
        return createdProcessManagerKey;
    }

    private void setCreatedProcessManagerKey(ProcessManagerKey createdProcessManagerKey) {
        this.createdProcessManagerKey = createdProcessManagerKey;
    }

    public boolean hasCreatedProcessManagerKey() {
        return createdProcessManagerKey != null;
    }

    @Override
    public String toString() {
        return "SuccessfulConsumption [consumedMessage=" + consumedMessage + ", createdProcessManagerKey="
                + createdProcessManagerKey + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumedMessage == null) ? 0 : consumedMessage.hashCode());
        result = prime * result + ((createdProcessManagerKey == null) ? 0 : createdProcessManagerKey.hashCode());
        return result;
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
        SuccessfulConsumption other = (SuccessfulConsumption) obj;
        if (consumedMessage == null) {
            if (other.consumedMessage != null) {
                return false;
            }
        } else if (!consumedMessage.equals(other.consumedMessage)) {
            return false;
        }
        if (createdProcessManagerKey == null) {
            if (other.createdProcessManagerKey != null) {
                return false;
            }
        } else if (!createdProcessManagerKey.equals(other.createdProcessManagerKey)) {
            return false;
        }
        return true;
    }
}
