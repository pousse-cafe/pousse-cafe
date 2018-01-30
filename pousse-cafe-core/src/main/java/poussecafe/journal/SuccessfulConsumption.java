package poussecafe.journal;

import poussecafe.messaging.Message;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SuccessfulConsumption {

    private Message consumedMessage;

    public SuccessfulConsumption(Message consumedMessage) {
        setConsumedMessage(consumedMessage);
    }

    public Message getConsumedMessage() {
        return consumedMessage;
    }

    private void setConsumedMessage(Message consumedMessage) {
        checkThat(value(consumedMessage).notNull().because("Consumed message cannot be null"));
        this.consumedMessage = consumedMessage;
    }

    @Override
    public String toString() {
        return "SuccessfulConsumption [consumedMessage=" + consumedMessage + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumedMessage == null) ? 0 : consumedMessage.hashCode());
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
        return true;
    }
}
