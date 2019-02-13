package poussecafe.messaging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.Environment;
import poussecafe.exception.PousseCafeException;

import static java.util.Collections.emptySet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageListenerRegistry {

    private Map<Class<? extends Message>, Set<MessageListener>> listeners = new HashMap<>();

    public void registerListener(MessageListener entry) {
        logger.debug("Registering listener {}", entry);
        if(!environment.getDefinedMessages().contains(entry.messageClass())) {
            throw new PousseCafeException("Message " + entry.messageClass().getName() + " is not defined");
        }
        Set<MessageListener> registeredListeners = getOrCreateSet(entry.messageClass());
        registeredListeners.add(entry);
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
        Class<? extends Message> messageClass = environment.getMessageClass(messageImplementationClass);
        if(messageClass == null) {
            return getListenersForMessageClass(messageImplementationClass);
        } else {
            return getListenersForMessageClass(messageClass);
        }
    }

    private Environment environment;

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Set<MessageListener> getListenersForMessageClass(Class<? extends Message> key) {
        return Optional.ofNullable(listeners.get(key)).orElse(emptySet());
    }
}
