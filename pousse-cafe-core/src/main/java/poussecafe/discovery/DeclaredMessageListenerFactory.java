package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;

public class DeclaredMessageListenerFactory extends AnnotatedMethodServiceMessageListenerFactory {

    @Override
    protected Consumer<Message> buildMessageConsumer(Object target,
            Method method) {
        return message -> {
            try {
                method.invoke(target, message);
            } catch (Exception e) {
                throw new PousseCafeException("Unable to invoke declared listener", e);
            }
        };
    }
}
