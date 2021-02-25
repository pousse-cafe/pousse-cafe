package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.Optional;
import poussecafe.environment.DeclaredMessageListenerIdBuilder;
import poussecafe.environment.MessageConsumer;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.environment.MessageListenerType;
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
                .consumedMessageClass(messageClass)
                .type(MessageListenerType.CUSTOM)
                .consumer(buildMessageConsumer(target, method, listenerShortId))
                .collisionSpace(collisionSpace)
                .build();
    }

    protected MessageConsumer buildMessageConsumer(Object target,
            Method method, String listenerShortId) {
        MessageListenerDefinition.checkMethodIsListener(method);
        return state -> {
            try {
                synchronized(target) {
                    method.invoke(target, state.message().original());
                }
                return MessageListenerConsumptionReport.success(listenerShortId);
            } catch (Exception e) {
                throw new PousseCafeException("Unable to invoke declared listener", e);
            }
        };
    }
}
