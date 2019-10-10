package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import poussecafe.environment.DeclaredMessageListenerIdBuilder;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.environment.MessageListenerPriority;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;

public class CustomMessageListenerFactory {

    public poussecafe.environment.MessageListener build(Object target,
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
                    .declaringClass(target.getClass())
                    .build();
        }

        String listenerShortId = annotation.shortId();
        if(listenerShortId == null || listenerShortId.isEmpty()) {
            listenerShortId = new DeclaredMessageListenerIdBuilder()
                    .messageClass(messageClass)
                    .method(method)
                    .declaringClass(target.getClass())
                    .buildShortId();
        }

        Optional<String> collisionSpace = Optional.empty();
        if(!annotation.collisionSpace().isBlank()) {
            collisionSpace = Optional.of(annotation.collisionSpace());
        }

        return new poussecafe.environment.MessageListener.Builder()
                .id(listenerId)
                .shortId(listenerShortId)
                .messageClass(messageClass)
                .priority(MessageListenerPriority.CUSTOM)
                .consumer(buildMessageConsumer(target, method))
                .collisionSpace(collisionSpace)
                .build();
    }

    protected Consumer<Message> buildMessageConsumer(Object target,
            Method method) {
        MessageListenerDefinition.checkMethodIsListener(method);
        return message -> {
            try {
                method.invoke(target, message);
            } catch (Exception e) {
                throw new PousseCafeException("Unable to invoke declared listener", e);
            }
        };
    }
}
