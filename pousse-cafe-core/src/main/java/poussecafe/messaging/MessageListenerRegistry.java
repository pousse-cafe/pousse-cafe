package poussecafe.messaging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.Environment;
import poussecafe.context.MessageListenerEntry;

import static java.util.Collections.emptySet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageListenerRegistry {

    private Map<MessageListenerRoutingKey, Set<MessageListener>> listeners = new HashMap<>();

    public void registerListener(MessageListenerEntry entry) {
        logger.debug("Registring listener {}", entry);
        Set<MessageListener> registeredListeners = getOrCreateSet(entry.getKey());
        registeredListeners.add(entry.getListener());
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Set<MessageListener> getOrCreateSet(MessageListenerRoutingKey key) {
        return listeners.computeIfAbsent(key, this::putNewEmptySet);
    }

    private Set<MessageListener> putNewEmptySet(MessageListenerRoutingKey key) {
        Set<MessageListener> messageListeners = new HashSet<>();
        listeners.put(key, messageListeners);
        return messageListeners;
    }

    public Set<MessageListener> getListeners(Class<? extends Message> messageImplementationClass) {
        Class<? extends Message> messageClass = environment.getMessageClass(messageImplementationClass);
        if(messageClass == null) {
            return getListeners(new MessageListenerRoutingKey(messageImplementationClass));
        } else {
            return getListeners(new MessageListenerRoutingKey(messageClass));
        }
    }

    private Environment environment;

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Set<MessageListener> getListeners(MessageListenerRoutingKey key) {
        return Optional.ofNullable(listeners.get(key)).orElse(emptySet());
    }
}
