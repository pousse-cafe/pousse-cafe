package poussecafe.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.Environment;

import static java.util.Collections.emptySet;

public class MessageListenerRegistry {

    private Map<Class<? extends Message>, Set<MessageListener>> listeners = new HashMap<>();

    public void registerListener(MessageListener listener) {
        logger.debug("Registering listener {}", listener);
        Set<MessageListener> registeredListeners = getOrCreateSet(listener.messageClass());
        registeredListeners.add(listener);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Set<MessageListener> getOrCreateSet(Class<? extends Message> key) {
        return listeners.computeIfAbsent(key, this::putNewEmptySet);
    }

    private Set<MessageListener> putNewEmptySet(Class<? extends Message> key) {
        Set<MessageListener> messageListeners = new HashSet<>();
        listeners.put(key, messageListeners);
        return messageListeners;
    }

    public Set<MessageListener> getListeners(Class<? extends Message> messageImplementationClass) {
        Class<? extends Message> messageClass = environment.definedMessageClass(messageImplementationClass);
        if(messageClass == null) {
            return getListenersForMessageClass(messageImplementationClass);
        } else {
            return getListenersForMessageClass(messageClass);
        }
    }

    private Environment environment;

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Set<MessageListener> getListenersForMessageClass(Class<? extends Message> key) {
        return Optional.ofNullable(listeners.get(key)).map(Collections::unmodifiableSet).orElse(emptySet());
    }

    public Collection<MessageListener> allListeners() {
        List<MessageListener> allListeners = new ArrayList<>();
        listeners.values().forEach(allListeners::addAll);
        return allListeners;
    }
}
