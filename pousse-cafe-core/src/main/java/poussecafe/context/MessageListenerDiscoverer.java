package poussecafe.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.MessageListener;

class MessageListenerDiscoverer {

    static class Builder {

        private MessageListenerDiscoverer discoverer = new MessageListenerDiscoverer();

        public Builder service(Object service) {
            discoverer.service = service;
            return this;
        }

        public Builder messageListenerFactory(MessageListenerFactory messageListenerFactory) {
            discoverer.messageListenerFactory = messageListenerFactory;
            return this;
        }

        public MessageListenerDiscoverer build() {
            Objects.requireNonNull(discoverer.service);
            Objects.requireNonNull(discoverer.messageListenerFactory);
            return discoverer;
        }
    }

    private MessageListenerDiscoverer() {

    }

    private Object service;

    public List<MessageListener> discoverListeners() {
        List<MessageListener> listeners = new ArrayList<>();
        for (Method method : service.getClass().getMethods()) {
            MessageListener listener = tryDiscoverListener(method);
            if (listener != null) {
                listeners.add(listener);
            }
        }
        return listeners;
    }

    private MessageListener tryDiscoverListener(Method method) {
        DomainEventListener annotation = method.getAnnotation(DomainEventListener.class);
        if (annotation != null) {
            return messageListenerFactory.build(service, method);
        } else {
            return null;
        }
    }

    private MessageListenerFactory messageListenerFactory;
}
