package poussecafe.environment;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class AggregateListeners {

    public void include(MessageListener listener) {
        Class messageClass = listener.messageClass();
        MessageListeners listeners = messagesListeners.computeIfAbsent(messageClass, key -> new MessageListeners());
        listeners.include(listener);
    }

    private Map<Class, MessageListeners> messagesListeners = new HashMap<>();
}
