package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;

import static poussecafe.util.ReferenceEquals.referenceEquals;

public class MessageListener implements Comparable<MessageListener> {

    public static class Builder {

        private MessageListener listener = new MessageListener();

        public Builder id(String id) {
            listener.id = id;
            return this;
        }

        public Builder messageClass(Class<? extends Message> messageClass) {
            listener.messageClass = messageClass;
            return this;
        }

        public Builder consumer(Consumer<Message> consumer) {
            listener.consumer = consumer;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder runner(Optional<AggregateMessageListenerRunner> runner) {
            listener.runner = runner;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        private Integer priority;

        public MessageListener build() {
            Objects.requireNonNull(listener.id);
            Objects.requireNonNull(listener.messageClass);
            Objects.requireNonNull(listener.consumer);
            Objects.requireNonNull(listener.runner);

            Objects.requireNonNull(priority);
            listener.priority = priority;

            return listener;
        }
    }

    private MessageListener() {

    }

    private String id;

    public String id() {
        return id;
    }

    private Class<? extends Message> messageClass;

    public Class<? extends Message> messageClass() {
        return messageClass;
    }

    private Consumer<Message> consumer;

    public Consumer<Message> consumer() {
        return consumer;
    }

    @SuppressWarnings("rawtypes")
    private Optional<AggregateMessageListenerRunner> runner = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<AggregateMessageListenerRunner> runner() {
        return runner;
    }

    private int priority;

    public int priority() {
        return priority;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(MessageListener o) {
        return priority - o.priority;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(id, other.id)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .build();
    }
}
