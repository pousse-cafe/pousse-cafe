package poussecafe.context;

import java.lang.reflect.Method;
import poussecafe.messaging.MessageListener;

@FunctionalInterface
public interface ClassMessageListenerFactory {

    MessageListener build(Method method);
}
