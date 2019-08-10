package poussecafe.environment;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.messaging.Message;
import poussecafe.util.ReflectionUtils;

public class MessageListenerRegistry {

    public void registerListener(MessageListener listener) {
        logger.debug("Registering listener {}", listener);

        Class<? extends Message> messageClass = listener.messageClass();
        messageListenersPool.registerListenerForMessageClass(listener, messageClass);

        if(ReflectionUtils.isAbstract(messageClass)) {
            Class<? extends Message> messageImplementationClass = environment.messageImplementationClass(messageClass);
            messageListenersPool.registerListenerForMessageClass(listener, messageImplementationClass);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Environment environment;

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private MessageListenersPool messageListenersPool = new MessageListenersPool();

    public Set<MessageListener> getListeners(Class<? extends Message> messageClass) {
        return messageListenersPool.getListeners(messageClass);
    }

    public Collection<MessageListener> allListeners() {
        return messageListenersPool.allListeners();
    }

    public MessageListenersPool messageListenersPool() {
        return messageListenersPool;
    }
}
