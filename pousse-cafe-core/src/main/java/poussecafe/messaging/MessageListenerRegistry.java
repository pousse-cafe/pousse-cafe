package poussecafe.messaging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import poussecafe.context.MessageListenerEntry;

import static java.util.Collections.emptySet;

public class MessageListenerRegistry {

    private Map<MessageListenerRoutingKey, Set<MessageListener>> listeners = new HashMap<>();

    public void registerListener(MessageListenerEntry entry) {
        Set<MessageListener> registeredListeners = getOrCreateSet(entry.getKey());
        registeredListeners.add(entry.getListener());
    }

    private Set<MessageListener> getOrCreateSet(MessageListenerRoutingKey key) {
        Set<MessageListener> messageListeners = listeners.get(key);
        if (messageListeners == null) {
            messageListeners = new HashSet<>();
            listeners.put(key, messageListeners);
        }
        return messageListeners;
    }

    public Set<MessageListener> getListeners(MessageListenerRoutingKey key) {
        return Optional.ofNullable(listeners.get(key)).orElse(emptySet());
    }
}
