package poussecafe.messaging;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.context.AggregateMessageListenerRunner;

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

        public Builder runner(Optional<Class<? extends AggregateMessageListenerRunner>> runner) {
            definition.runner = runner;
            return this;
        }

        public MessageListenerDefinition build() {
            Objects.requireNonNull(definition.method);
            Objects.requireNonNull(definition.customId);
            Objects.requireNonNull(definition.runner);
            return definition;
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

    private Optional<Class<? extends AggregateMessageListenerRunner>> runner = Optional.empty();

    public Optional<Class<? extends AggregateMessageListenerRunner>> runner() {
        return runner;
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
