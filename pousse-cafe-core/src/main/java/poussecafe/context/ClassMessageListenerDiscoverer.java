package poussecafe.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.MessageListener;

public class ClassMessageListenerDiscoverer {

    public static class Builder {

        private ClassMessageListenerDiscoverer discoverer = new ClassMessageListenerDiscoverer();

        public Builder targetClass(Class<?> targetClass) {
            discoverer.targetClass = targetClass;
            return this;
        }

        public Builder messageListenerFactory(ClassMessageListenerFactory messageListenerFactory) {
            discoverer.messageListenerFactory = messageListenerFactory;
            return this;
        }

        public ClassMessageListenerDiscoverer build() {
            Objects.requireNonNull(discoverer.targetClass);
            Objects.requireNonNull(discoverer.messageListenerFactory);
            return discoverer;
        }
    }

    private ClassMessageListenerDiscoverer() {

    }

    private Class<?> targetClass;

    public List<MessageListener> discoverListeners() {
        List<MessageListener> listeners = new ArrayList<>();
        for (Method method : targetClass.getMethods()) {
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
            return messageListenerFactory.build(method);
        } else {
            return null;
        }
    }

    private ClassMessageListenerFactory messageListenerFactory;
}
