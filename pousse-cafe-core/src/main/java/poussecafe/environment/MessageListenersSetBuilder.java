package poussecafe.environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import poussecafe.messaging.Message;
import poussecafe.processing.ListenersSet;

import static java.util.Collections.emptySet;

public class MessageListenersSetBuilder implements ListenersSet {

    public synchronized void registerListenerForMessageClass(MessageListener listener, Class<? extends Message> messageClass) {
        Set<MessageListener> messageClassListeners = listenersByMessageClass.computeIfAbsent(messageClass, key -> new HashSet<>());
        if(!messageClassListeners.add(listener)) {
            throw new IllegalArgumentException("Listener could not be registered for message " + messageClass.getName() + ", would hide another one: " + listener);
        }

        Set<Class<? extends Message>> messageClasses = messageClassesByListener.computeIfAbsent(listener, key -> new HashSet<>());
        if(!messageClasses.add(messageClass)) {
            throw new IllegalArgumentException("Listener " + listener + " already linked to message " + messageClass);
        }
    }

    private Map<Class<? extends Message>, Set<MessageListener>> listenersByMessageClass = new HashMap<>();

    @Override
    public synchronized Set<MessageListener> messageListenersOf(Class<? extends Message> messageClass) {
        return getListenersForMessageClass(messageClass);
    }

    private Set<MessageListener> getListenersForMessageClass(Class<? extends Message> key) {
        return Optional.ofNullable(listenersByMessageClass.get(key)).map(Collections::unmodifiableSet).orElse(emptySet());
    }

    @Override
    public synchronized Collection<MessageListener> messageListeners() {
        return Collections.unmodifiableSet(messageClassesByListener.keySet());
    }

    private Map<MessageListener, Set<Class<? extends Message>>> messageClassesByListener = new HashMap<>();

    @Override
    public synchronized boolean contains(MessageListener listener) {
        return messageClassesByListener.keySet().contains(listener);
    }

    public synchronized int countListeners() {
        return messageClassesByListener.size();
    }

    public synchronized Map<MessageListener, Set<Class<? extends Message>>> messageClassesByListener() {
        return Collections.unmodifiableMap(messageClassesByListener);
    }
}
