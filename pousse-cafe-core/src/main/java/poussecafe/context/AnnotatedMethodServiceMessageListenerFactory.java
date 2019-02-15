package poussecafe.context;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;

public abstract class AnnotatedMethodServiceMessageListenerFactory implements ServiceMessageListenerFactory {

    @Override
    public MessageListener build(Object target,
            Method method) {
        DomainEventListener annotation = method.getAnnotation(DomainEventListener.class);
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
        return new MessageListener.Builder()
                .id(listenerId)
                .messageClass(messageClass)
                .consumer(buildMessageConsumer(target, method))
                .build();
    }

    protected abstract Consumer<Message> buildMessageConsumer(Object target,
            Method method);
}
