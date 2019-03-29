package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomMessageListenerDiscoverer {

    public static class Builder {

        private CustomMessageListenerDiscoverer discoverer = new CustomMessageListenerDiscoverer();

        public Builder service(Object service) {
            discoverer.service = service;
            return this;
        }

        public CustomMessageListenerDiscoverer build() {
            Objects.requireNonNull(discoverer.service);
            return discoverer;
        }
    }

    private CustomMessageListenerDiscoverer() {

    }

    private Object service;

    public List<poussecafe.environment.MessageListener> discoverListeners() {
        List<poussecafe.environment.MessageListener> listeners = new ArrayList<>();
        for (Method method : service.getClass().getMethods()) {
            poussecafe.environment.MessageListener listener = tryDiscoverListener(method);
            if (listener != null) {
                listeners.add(listener);
            }
        }
        return listeners;
    }

    private poussecafe.environment.MessageListener tryDiscoverListener(Method method) {
        MessageListener annotation = method.getAnnotation(MessageListener.class);
        if (annotation != null) {
            return messageListenerFactory.build(service, method);
        } else {
            return null;
        }
    }

    private CustomMessageListenerFactory messageListenerFactory = new CustomMessageListenerFactory();
}
