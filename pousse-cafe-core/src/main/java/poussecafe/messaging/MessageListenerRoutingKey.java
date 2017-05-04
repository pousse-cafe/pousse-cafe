package poussecafe.messaging;

public class MessageListenerRoutingKey {

    private Queue source;

    private Class<? extends Message> messageClass;

    public MessageListenerRoutingKey(Queue source, Class<? extends Message> messageClass) {
        setSource(source);
        setMessageClass(messageClass);
    }

    public Queue getSource() {
        return source;
    }

    private void setSource(Queue source) {
        this.source = source;
    }

    public Class<? extends Message> getMessageClass() {
        return messageClass;
    }

    private void setMessageClass(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
    }

    @Override
    public String toString() {
        return "MessageListenerRoutingKey [source=" + source + ", messageClass=" + messageClass + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((messageClass == null) ? 0 : messageClass.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

}
