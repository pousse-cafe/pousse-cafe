package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class MessageDefinition implements NamedComponent, Serializable {

    private String messageName;

    @Override
    public String name() {
        return messageName;
    }

    private SourceLine sourceLine;

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    public String qualifiedClassName() {
        return qualifiedClassName;
    }

    private String qualifiedClassName;

    @Override
    public Name className() {
        return new Name(qualifiedClassName);
    }

    public boolean isEvent() {
        return domainEvent;
    }

    private boolean domainEvent;

    public static class Builder {

        public MessageDefinition build() {
            requireNonNull(definition.messageName);
            requireNonNull(definition.qualifiedClassName);
            return definition;
        }

        private MessageDefinition definition = new MessageDefinition();

        public Builder messageName(String messageName) {
            definition.messageName = messageName;
            return this;
        }

        public Builder sourceLine(SourceLine sourceLine) {
            definition.sourceLine = sourceLine;
            return this;
        }

        public Builder qualifiedClassName(String qualifiedClassName) {
            definition.qualifiedClassName = qualifiedClassName;
            return this;
        }

        public Builder domainEvent(boolean domainEvent) {
            definition.domainEvent = domainEvent;
            return this;
        }
    }

    private MessageDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(messageName, other.messageName)
                .append(sourceLine, other.sourceLine)
                .append(qualifiedClassName, other.qualifiedClassName)
                .append(domainEvent, other.domainEvent)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(messageName)
                .append(sourceLine)
                .append(qualifiedClassName)
                .append(domainEvent)
                .build();
    }
}
