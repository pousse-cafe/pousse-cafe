package poussecafe.domain;

import java.util.Objects;
import poussecafe.context.Environment;
import poussecafe.messaging.Message;
import poussecafe.util.ReflectionUtils;

public class MessageFactory {

    public MessageFactory(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Environment environment;

    @SuppressWarnings("unchecked")
    public <T extends Message> T newMessage(Class<T> messageClass) {
        return (T) ReflectionUtils.newInstance(environment.getMessageImplementationClass(messageClass));
    }
}
