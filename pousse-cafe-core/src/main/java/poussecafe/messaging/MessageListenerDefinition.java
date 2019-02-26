package poussecafe.messaging;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.AggregateMessageListenerRunner;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class MessageListenerDefinition {

    public static class Builder {

        private MessageListenerDefinition definition = new MessageListenerDefinition();

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

        public MessageListenerDefinition build() {
            checkMethodIsListener();
            Objects.requireNonNull(definition.method);
            Objects.requireNonNull(definition.customId);
            Objects.requireNonNull(definition.runner);
            return definition;
        }

        private void checkMethodIsListener() {
            if(definition.method.getParameterCount() != 1) {
                throw new PousseCafeException("A message listener takes exactly one argument");
            }
            Class<?> argumentType = definition.method.getParameterTypes()[0];
            if(!Message.class.isAssignableFrom(argumentType)) {
                throw new PousseCafeException("A message listener only accepts a sub-class of Message as argument");
            }
        }
    }

    private MessageListenerDefinition() {

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

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(method, other.method)
                .append(customId, other.customId)
                .append(runner, other.runner)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(method)
                .append(customId)
                .append(runner)
                .build();
    }
}
