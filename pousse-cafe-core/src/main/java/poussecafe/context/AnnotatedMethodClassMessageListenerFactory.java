package poussecafe.context;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;

public abstract class AnnotatedMethodClassMessageListenerFactory implements ClassMessageListenerFactory {

    @Override
    public MessageListener build(Method method) {
        DomainEventListener annotationWrapper = method.getAnnotation(DomainEventListener.class);
        @SuppressWarnings("unchecked")
        Class<? extends Message> messageClass = (Class<? extends Message>) method.getParameters()[0].getType();
        String listenerId = annotationWrapper.id();
        if(listenerId == null || listenerId.isEmpty()) {
            listenerId = new DeclaredMessageListenerIdBuilder()
                    .messageClass(messageClass)
                    .method(method)
                    .build();
        }
        return new MessageListener.Builder()
                .id(listenerId)
                .messageClass(messageClass)
                .consumer(buildMessageConsumer(method))
                .build();
    }

    protected abstract Consumer<Message> buildMessageConsumer(Method method);
}
