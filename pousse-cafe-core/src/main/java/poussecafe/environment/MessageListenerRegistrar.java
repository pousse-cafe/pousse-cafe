package poussecafe.environment;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.messaging.Message;
import poussecafe.processing.ListenersSet;
import poussecafe.util.ReflectionUtils;

public class MessageListenerRegistrar {

    public void registerListener(MessageListener listener) {
        logger.debug("Registering listener {}", listener);

        Class<? extends Message> messageClass = listener.consumedMessageClass();
        messageListenersSetBuilder.registerListenerForMessageClass(listener, messageClass);

        if(ReflectionUtils.isAbstract(messageClass)) {
            Class<? extends Message> messageImplementationClass = environment.messageImplementationClass(messageClass);
            messageListenersSetBuilder.registerListenerForMessageClass(listener, messageImplementationClass);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Environment environment;

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private MessageListenersSetBuilder messageListenersSetBuilder = new MessageListenersSetBuilder();

    public Set<MessageListener> getListeners(Class<? extends Message> messageClass) {
        return messageListenersSetBuilder.messageListenersOf(messageClass);
    }

    public Collection<MessageListener> allListeners() {
        return messageListenersSetBuilder.messageListeners();
    }

    public ListenersSet listenersSet() {
        return messageListenersSetBuilder;
    }
}
