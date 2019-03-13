package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import poussecafe.environment.DeclaredMessageListenerIdBuilder;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;

public abstract class AnnotatedMethodServiceMessageListenerFactory implements ServiceMessageListenerFactory {

    @Override
    public poussecafe.messaging.MessageListener build(Object target,
            Method method) {
        MessageListener annotation = method.getAnnotation(MessageListener.class);
        if(annotation == null) {
            throw new PousseCafeException("Invalid service message listener");
        }
        @SuppressWarnings("unchecked")
        Class<? extends Message> messageClass = (Class<? extends Message>) method.getParameters()[0].getType();
        String listenerId = annotation.id();
        if(listenerId == null || listenerId.isEmpty()) {
            listenerId = new DeclaredMessageListenerIdBuilder()
                    .messageClass(messageClass)
                    .method(method)
                    .build();
        }
        return new poussecafe.messaging.MessageListener.Builder()
                .id(listenerId)
                .messageClass(messageClass)
                .consumer(buildMessageConsumer(target, method))
                .build();
    }

    protected abstract Consumer<Message> buildMessageConsumer(Object target,
            Method method);
}
