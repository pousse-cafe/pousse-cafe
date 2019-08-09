package poussecafe.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import poussecafe.messaging.Message;

import static java.util.Collections.emptySet;

public class MessageListenersPool {

    public void registerListenerForMessageClass(MessageListener listener, Class<? extends Message> messageClass) {
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

    public Set<MessageListener> getListeners(Class<? extends Message> messageClass) {
        return getListenersForMessageClass(messageClass);
    }

    private Set<MessageListener> getListenersForMessageClass(Class<? extends Message> key) {
        return Optional.ofNullable(listenersByMessageClass.get(key)).map(Collections::unmodifiableSet).orElse(emptySet());
    }

    public Collection<MessageListener> allListeners() {
        List<MessageListener> allListeners = new ArrayList<>();
        listenersByMessageClass.values().stream().distinct().forEach(allListeners::addAll);
        return allListeners;
    }

    public MessageListenersPool[] split(int expectedNumberOfPartitions) {
        int totalListeners = messageClassesByListener.size();
        int listenersPerPool = Math.max(totalListeners / expectedNumberOfPartitions, 1);
        MessageListenersPool[] pools = new MessageListenersPool[expectedNumberOfPartitions];
        Iterator<Entry<MessageListener, Set<Class<? extends Message>>>> listenersIterator = messageClassesByListener.entrySet().iterator();
        for(int poolIndex = 0; poolIndex < pools.length; ++poolIndex) {
            if(!listenersIterator.hasNext()) {
                break;
            }

            pools[poolIndex] = new MessageListenersPool();
            for(int listenersInCurrentPool = 0; listenersInCurrentPool < listenersPerPool; ++listenersInCurrentPool) {
                if(!listenersIterator.hasNext()) {
                    break;
                }

                Entry<MessageListener, Set<Class<? extends Message>>> entry = listenersIterator.next();
                MessageListener listener = entry.getKey();
                Set<Class<? extends Message>> messages = entry.getValue();
                for(Class<? extends Message> messageClass : messages) {
                    pools[poolIndex].registerListenerForMessageClass(listener, messageClass);
                }
            }
        }
        return pools;
    }

    private Map<MessageListener, Set<Class<? extends Message>>> messageClassesByListener = new HashMap<>();

    public boolean contains(MessageListener listener) {
        return messageClassesByListener.keySet().contains(listener);
    }
}
