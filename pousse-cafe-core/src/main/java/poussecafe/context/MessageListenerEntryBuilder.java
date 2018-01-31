package poussecafe.context;

import java.lang.reflect.Method;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRoutingKey;

public class MessageListenerEntryBuilder {

    private Class<? extends Message> messageClass;

    private String listenerId;

    private Method method;

    private Object target;

    public MessageListenerEntry build() {
        MessageListenerRoutingKey routingKey = new MessageListenerRoutingKey(messageClass);
        MessageListener listener = new MessageListener(getOrGenerateListenerId(), method, target);
        return new MessageListenerEntry(routingKey, listener);
    }

    private String getOrGenerateListenerId() {
        if (listenerId == null || listenerId.isEmpty()) {
            return generateListenerId();
        } else {
            return listenerId;
        }
    }

    private String generateListenerId() {
        return target.getClass().getCanonicalName() + "::" + method.getName() + "("
                + messageClass.getCanonicalName() + ")";
    }

    public MessageListenerEntryBuilder withMessageClass(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
        return this;
    }

    public MessageListenerEntryBuilder withListenerId(String listenerId) {
        this.listenerId = listenerId;
        return this;
    }

    public MessageListenerEntryBuilder withMethod(Method method) {
        this.method = method;
        return this;
    }

    public MessageListenerEntryBuilder withTarget(Object target) {
        this.target = target;
        return this;
    }
}
