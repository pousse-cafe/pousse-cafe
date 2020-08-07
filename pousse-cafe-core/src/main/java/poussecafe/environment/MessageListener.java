package poussecafe.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.messaging.Message;
import poussecafe.util.ClassHierarchyTypeArgumentsValidator;

import static poussecafe.util.Equality.referenceEquals;

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

        public Builder consumedMessageClass(Class<? extends Message> consumedMessageClass) {
            listener.consumedMessageClass = consumedMessageClass;
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

        public Builder type(MessageListenerType type) {
            listener.type = type;
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

        public Builder expectedEvents(List<ExpectedEvent> expectedEvents) {
            listener.expectedEvents = new ArrayList<>(expectedEvents);
            return this;
        }

        public Builder withExpectedEvents(boolean withExpectedEvents) {
            listener.withExpectedEvents = withExpectedEvents;
            return this;
        }

        public MessageListener build() {
            Objects.requireNonNull(listener.id);
            Objects.requireNonNull(listener.shortId);
            Objects.requireNonNull(listener.consumedMessageClass);
            Objects.requireNonNull(listener.consumer);
            Objects.requireNonNull(listener.runner);
            Objects.requireNonNull(listener.type);
            Objects.requireNonNull(listener.collisionSpace);
            Objects.requireNonNull(listener.aggregateRootClass);
            Objects.requireNonNull(listener.expectedEvents);

            if((listener.type == MessageListenerType.AGGREGATE
                    || listener.type == MessageListenerType.FACTORY
                    || listener.type == MessageListenerType.REPOSITORY)
                && listener.aggregateRootClass.isEmpty()) {
                throw new IllegalStateException("Aggregate, factory or repository listeners require an aggregate root class");
            }

            if((listener.type != MessageListenerType.AGGREGATE
                    && listener.type != MessageListenerType.FACTORY
                    && listener.type != MessageListenerType.REPOSITORY)
                && listener.withExpectedEvents) {
                throw new IllegalStateException("Cannot check produced events on a listener which is not on an Aggregate Root, a Factory or a Repository");
            }

            if(listener.type == MessageListenerType.AGGREGATE
                    && listener.runner.isEmpty()) {
                throw new IllegalStateException("Aggregate Root listeners must have a runner");
            }

            validRunnerOrThrow();

            return listener;
        }

        private void validRunnerOrThrow() {
            if(listener.type == MessageListenerType.AGGREGATE) {
                var runner = listener.runner.orElseThrow();
                var validator = new ClassHierarchyTypeArgumentsValidator.Builder()
                    .listenerId(listener.id)
                    .expectedTypeArgument(listener.aggregateRootClass.orElseThrow())
                    .expectedTypeArgument(listener.consumedMessageClass)
                    .runner(runner.getClass())
                    .build();
                validator.validOrThrow();
            }
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

    private Class<? extends Message> consumedMessageClass;

    public Class<? extends Message> consumedMessageClass() {
        return consumedMessageClass;
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

    private MessageListenerType type;

    public MessageListenerType type() {
        return type;
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

    public List<ExpectedEvent> expectedEvents() {
        return Collections.unmodifiableList(expectedEvents);
    }

    private List<ExpectedEvent> expectedEvents = Collections.emptyList();

    public boolean withExpectedEvents() {
        return withExpectedEvents;
    }

    private boolean withExpectedEvents;

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(MessageListener o) {
        return type.compareTo(o.type);
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
