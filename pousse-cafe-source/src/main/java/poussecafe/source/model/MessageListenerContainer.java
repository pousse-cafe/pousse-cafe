package poussecafe.source.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class MessageListenerContainer {

    private MessageListenerContainer() {

    }

    public Optional<String> aggregateName() {
        return aggregateName;
    }

    private Optional<String> aggregateName;

    public MessageListenerContainerType type() {
        return type;
    }

    private MessageListenerContainerType type;

    public String className() {
        return className;
    }

    private String className;

    public static class Builder {

        private MessageListenerContainer container = new MessageListenerContainer();

        public MessageListenerContainer build() {
            requireNonNull(container.aggregateName);
            requireNonNull(container.type);
            requireNonNull(container.className);

            if((container.type == MessageListenerContainerType.FACTORY
                        || container.type == MessageListenerContainerType.REPOSITORY
                        || container.type == MessageListenerContainerType.ROOT)
                    && container.aggregateName.isEmpty()) {
                throw new IllegalStateException("Aggregate name must be set with type " + container.type);
            }

            return container;
        }

        public Builder aggregateName(String aggregateName) {
            container.aggregateName = Optional.of(aggregateName);
            return this;
        }

        public Builder type(MessageListenerContainerType type) {
            container.type = type;
            return this;
        }

        public Builder className(String className) {
            container.className = className;
            return this;
        }
    }
}
