package poussecafe.runtime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.MessageListener;

public class ServiceMessageListenerDiscoverer {

    public static class Builder {

        private ServiceMessageListenerDiscoverer discoverer = new ServiceMessageListenerDiscoverer();

        public Builder service(Object service) {
            discoverer.service = service;
            return this;
        }

        public Builder messageListenerFactory(ServiceMessageListenerFactory messageListenerFactory) {
            discoverer.messageListenerFactory = messageListenerFactory;
            return this;
        }

        public ServiceMessageListenerDiscoverer build() {
            Objects.requireNonNull(discoverer.service);
            Objects.requireNonNull(discoverer.messageListenerFactory);
            return discoverer;
        }
    }

    private ServiceMessageListenerDiscoverer() {

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

    private ServiceMessageListenerFactory messageListenerFactory;
}
