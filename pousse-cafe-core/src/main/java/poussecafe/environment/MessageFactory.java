package poussecafe.environment;

import java.util.Objects;
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
        return (T) ReflectionUtils.newInstance(environment.messageImplementationClass(messageClass));
    }
}
