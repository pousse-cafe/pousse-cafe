package poussecafe.messaging;

public class MessageListenerRoutingKey {

    private Class<? extends Message> messageClass;

    public MessageListenerRoutingKey(Class<? extends Message> messageClass) {
        setMessageClass(messageClass);
    }

    public Class<? extends Message> getMessageClass() {
        return messageClass;
    }

    private void setMessageClass(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((messageClass == null) ? 0 : messageClass.hashCode());
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
        MessageListenerRoutingKey other = (MessageListenerRoutingKey) obj;
        if (messageClass == null) {
            if (other.messageClass != null) {
                return false;
            }
        } else if (!messageClass.equals(other.messageClass)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MessageListenerRoutingKey [messageClass=" + messageClass + "]";
    }

}
