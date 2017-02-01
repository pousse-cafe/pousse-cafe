package poussecafe.configuration;

import poussecafe.consequence.ConsequenceListener;
import poussecafe.consequence.ConsequenceListenerRoutingKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceListenerEntry {

    private ConsequenceListenerRoutingKey key;

    private ConsequenceListener listener;

    public ConsequenceListenerEntry(ConsequenceListenerRoutingKey key, ConsequenceListener listener) {
        setKey(key);
        setListener(listener);
    }

    public ConsequenceListenerRoutingKey getKey() {
        return key;
    }

    private void setKey(ConsequenceListenerRoutingKey key) {
        checkThat(value(key).notNull().because("Consequence listener routing key cannot be null"));
        this.key = key;
    }

    public ConsequenceListener getListener() {
        return listener;
    }

    private void setListener(ConsequenceListener listener) {
        checkThat(value(listener).notNull().because("Consequence listener cannot be null"));
        this.listener = listener;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((listener == null) ? 0 : listener.hashCode());
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
        ConsequenceListenerEntry other = (ConsequenceListenerEntry) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (listener == null) {
            if (other.listener != null) {
                return false;
            }
        } else if (!listener.equals(other.listener)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConsequenceListenerEntry [key=" + key + ", listener=" + listener + "]";
    }
}
