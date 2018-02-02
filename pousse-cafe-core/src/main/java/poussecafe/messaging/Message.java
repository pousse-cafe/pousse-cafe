package poussecafe.messaging;

import java.util.UUID;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public abstract class Message {

    private String id;

    protected Message() {
        id = UUID.randomUUID().toString();
    }

    protected Message(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        checkThat(value(id).verifies(not(emptyOrNullString())).because("ID cannot be null or empty"));
        this.id = id;
    }

    public String getType() {
        return getClass().getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Message other = (Message) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + "]";
    }
}
