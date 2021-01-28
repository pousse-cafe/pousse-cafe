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

    public String containerIdentifier() {
        return containerIdentifier;
    }

    private String containerIdentifier;

    public boolean isQualifiedIdentifier() {
        return containerIdentifier.indexOf('.', 0) != -1;
    }

    public static class Builder {

        private MessageListenerContainer container = new MessageListenerContainer();

        public MessageListenerContainer build() {
            requireNonNull(container.aggregateName);
            requireNonNull(container.type);
            requireNonNull(container.containerIdentifier);

            if((container.type.isFactory()
                        || container.type.isRepository()
                        || container.type.isRoot())
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

        public Builder containerIdentifier(String containerIdentifier) {
            container.containerIdentifier = containerIdentifier;
            return this;
        }
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        if(type.isFactory()) {
            builder.append("F{");
        } else if(type.isRepository()) {
            builder.append("Re{");
        } else if(type.isRoot()) {
            builder.append("@");
        }

        builder.append(containerIdentifier);

        if(type.isFactory()
                || type.isRepository()) {
            builder.append("}");
        }
        return builder.toString();
    }
}
