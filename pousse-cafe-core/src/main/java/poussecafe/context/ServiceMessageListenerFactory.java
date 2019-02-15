package poussecafe.context;

import java.lang.reflect.Method;
import poussecafe.messaging.MessageListener;

@FunctionalInterface
public interface ServiceMessageListenerFactory {

    MessageListener build(Object target, Method method);
}
