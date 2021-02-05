package poussecafe.source.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class MessageListenerContainer implements Serializable {

    private MessageListenerContainer() {

    }

    public Optional<String> aggregateName() {
        return Optional.ofNullable(aggregateName);
    }

    private String aggregateName;

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
            requireNonNull(container.type);
            requireNonNull(container.containerIdentifier);

            if((container.type.isFactory()
                        || container.type.isRepository()
                        || container.type.isRoot())
                    && container.aggregateName == null) {
                throw new IllegalStateException("Aggregate name must be set with type " + container.type);
            }

            return container;
        }

        public Builder aggregateName(String aggregateName) {
            container.aggregateName = aggregateName;
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

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(aggregateName, other.aggregateName)
                .append(containerIdentifier, other.containerIdentifier)
                .append(type, other.type)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(aggregateName)
                .append(containerIdentifier)
                .append(type)
                .build();
    }
}
