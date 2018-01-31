package poussecafe.context;

import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRoutingKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageListenerEntry {

    private MessageListenerRoutingKey key;

    private MessageListener listener;

    public MessageListenerEntry(MessageListenerRoutingKey key, MessageListener listener) {
        setKey(key);
        setListener(listener);
    }

    public MessageListenerRoutingKey getKey() {
        return key;
    }

    private void setKey(MessageListenerRoutingKey key) {
        checkThat(value(key).notNull().because("Message listener routing key cannot be null"));
        this.key = key;
    }

    public MessageListener getListener() {
        return listener;
    }

    private void setListener(MessageListener listener) {
        checkThat(value(listener).notNull().because("Message listener cannot be null"));
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
        MessageListenerEntry other = (MessageListenerEntry) obj;
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
        return "MessageListenerEntry [key=" + key + ", listener=" + listener + "]";
    }
}
