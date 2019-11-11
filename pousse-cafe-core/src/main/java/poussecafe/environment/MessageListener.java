package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
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

        public Builder shortId(String shortId) {
            listener.shortId = shortId;
            return this;
        }

        public Builder messageClass(Class<? extends Message> messageClass) {
            listener.messageClass = messageClass;
            return this;
        }

        public Builder consumer(MessageConsumer consumer) {
            listener.consumer = consumer;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder runner(Optional<AggregateMessageListenerRunner> runner) {
            listener.runner = runner;
            return this;
        }

        public Builder priority(MessageListenerPriority priority) {
            listener.priority = priority;
            return this;
        }

        public Builder collisionSpace(Optional<String> collisionSpace) {
            listener.collisionSpace = collisionSpace;
            return this;
        }

        public Builder aggregateRootClass(@SuppressWarnings("rawtypes") Optional<Class> aggregateRootClass) {
            listener.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public MessageListener build() {
            Objects.requireNonNull(listener.id);
            Objects.requireNonNull(listener.shortId);
            Objects.requireNonNull(listener.messageClass);
            Objects.requireNonNull(listener.consumer);
            Objects.requireNonNull(listener.runner);
            Objects.requireNonNull(listener.priority);
            Objects.requireNonNull(listener.collisionSpace);
            Objects.requireNonNull(listener.aggregateRootClass);
            return listener;
        }
    }

    private MessageListener() {

    }

    private String id;

    public String id() {
        return id;
    }

    private String shortId;

    public String shortId() {
        return shortId;
    }

    private Class<? extends Message> messageClass;

    public Class<? extends Message> messageClass() {
        return messageClass;
    }

    private MessageConsumer consumer;

    public MessageConsumer consumer() {
        return consumer;
    }

    @SuppressWarnings("rawtypes")
    private Optional<AggregateMessageListenerRunner> runner = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<AggregateMessageListenerRunner> runner() {
        return runner;
    }

    private MessageListenerPriority priority;

    public MessageListenerPriority priority() {
        return priority;
    }

    private Optional<String> collisionSpace = Optional.empty();

    public Optional<String> collisionSpace() {
        return collisionSpace;
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateRootClass = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<Class> aggregateRootClass() {
        return aggregateRootClass;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(MessageListener o) {
        return priority.compareTo(o.priority);
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
