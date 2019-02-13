package poussecafe.context;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;

public class DeclaredMessageListenerFactory implements MessageListenerFactory {

    @Override
    public MessageListener build(Object target,
            Method method) {
        DomainEventListener annotationWrapper = method.getAnnotation(DomainEventListener.class);
        @SuppressWarnings("unchecked")
        Class<? extends Message> messageClass = (Class<? extends Message>) method.getParameters()[0].getType();
        String listenerId = annotationWrapper.id();
        if(listenerId == null || listenerId.isEmpty()) {
            listenerId = new DeclaredMessageListenerIdBuilder()
                    .messageClass(messageClass)
                    .target(target)
                    .method(method)
                    .build();
        }
        return new MessageListener.Builder()
                .id(listenerId)
                .messageClass(messageClass)
                .consumer(declaredListenerConsumer(target, method))
                .build();
    }

    private Consumer<Message> declaredListenerConsumer(Object target,
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
