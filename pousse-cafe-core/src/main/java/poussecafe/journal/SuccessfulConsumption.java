package poussecafe.journal;

import poussecafe.consequence.Consequence;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SuccessfulConsumption {

    private Consequence consumedConsequence;

    private ProcessManagerKey createdProcessManagerKey;

    public SuccessfulConsumption(Consequence consumedConsequence) {
        this(consumedConsequence, null);
    }

    public SuccessfulConsumption(Consequence consumedConsequence, ProcessManagerKey createdProcessManagerKey) {
        setConsumedConsequence(consumedConsequence);
        setCreatedProcessManagerKey(createdProcessManagerKey);
    }

    public Consequence getConsumedConsequence() {
        return consumedConsequence;
    }

    private void setConsumedConsequence(Consequence consumedConsequence) {
        checkThat(value(consumedConsequence).notNull().because("Consumed consequence cannot be null"));
        this.consumedConsequence = consumedConsequence;
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
        return "SuccessfulConsumption [consumedConsequence=" + consumedConsequence + ", createdProcessManagerKey="
                + createdProcessManagerKey + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumedConsequence == null) ? 0 : consumedConsequence.hashCode());
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
        if (consumedConsequence == null) {
            if (other.consumedConsequence != null) {
                return false;
            }
        } else if (!consumedConsequence.equals(other.consumedConsequence)) {
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
