package poussecafe.environment;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class MessageListenerDefinition {

    public static void checkMethodIsListener(Method method) {
        if(method.getParameterCount() != 1) {
            throw new PousseCafeException("Message listener " + method + " does not take exactly one argument");
        }

        Class<?> argumentType = method.getParameterTypes()[0];
        if(!Message.class.isAssignableFrom(argumentType)) {
            throw new PousseCafeException("Message listener " + method + " does not accept a sub-class of Message as argument");
        }

        if(!Modifier.isPublic(method.getModifiers())) {
            throw new PousseCafeException("Message listener " + method + " must be a public");
        }
    }

    public static class Builder {

        private MessageListenerDefinition definition = new MessageListenerDefinition();

        public Builder containerClass(Class<?> containerClass) {
            definition.containerClass = containerClass;
            return this;
        }

        public Builder method(Method method) {
            definition.method = method;
            return this;
        }

        public Builder customId(Optional<String> customId) {
            definition.customId = customId;
            return this;
        }

        public Builder runner(Optional<Class<? extends AggregateMessageListenerRunner<?, ?, ?>>> runner) {
            definition.runner = runner;
            return this;
        }

        public Builder collisionSpace(Optional<String> collisionSpace) {
            definition.collisionSpace = collisionSpace;
            return this;
        }

        public MessageListenerDefinition build() {
            Objects.requireNonNull(definition.containerClass);
            Objects.requireNonNull(definition.method);
            checkMethodIsListener(definition.method);
            Objects.requireNonNull(definition.customId);
            Objects.requireNonNull(definition.runner);
            return definition;
        }
    }

    private MessageListenerDefinition() {

    }

    private Class<?> containerClass;

    public Class<?> containerClass() {
        return containerClass;
    }

    private Method method;

    public Method method() {
        return method;
    }

    private Optional<String> customId = Optional.empty();

    public Optional<String> customId() {
        return customId;
    }

    private Optional<Class<? extends AggregateMessageListenerRunner<?, ?, ?>>> runner = Optional.empty();

    public Optional<Class<? extends AggregateMessageListenerRunner<?, ?, ?>>> runner() {
        return runner;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Message> messageClass() {
        return (Class<? extends Message>) method.getParameterTypes()[0];
    }

    public MessageListener.Builder messageListenerBuilder() {
        return new MessageListener.Builder()
                .id(id())
                .shortId(shortId())
                .consumedMessageClass(messageClass());
    }

    public String id() {
        if(!customId.isPresent()) {
            return declaredMessageListenerIdBuilder()
                    .build();
        } else {
            return customId.get();
        }
    }

    private DeclaredMessageListenerIdBuilder declaredMessageListenerIdBuilder() {
        return new DeclaredMessageListenerIdBuilder()
                .method(method)
                .declaringClass(containerClass)
                .messageClass(messageClass());
    }

    public String shortId() {
        if(!customId.isPresent()) {
            return declaredMessageListenerIdBuilder()
                    .buildShortId();
        } else {
            return customId.get();
        }
    }

    private Optional<String> collisionSpace = Optional.empty();

    public Optional<String> collisionSpace() {
        return collisionSpace;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(containerClass, other.containerClass)
                .append(method, other.method)
                .append(customId, other.customId)
                .append(runner, other.runner)
                .append(collisionSpace, other.collisionSpace)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(containerClass)
                .append(method)
                .append(customId)
                .append(runner)
                .append(collisionSpace)
                .build();
    }

    @Override
    public String toString() {
        return id();
    }
}
