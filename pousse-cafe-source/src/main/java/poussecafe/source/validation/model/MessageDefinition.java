package poussecafe.source.validation.model;

import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.names.NamedComponent;

import static java.util.Objects.requireNonNull;

public class MessageDefinition implements NamedComponent {

    private String messageName;

    @Override
    public String name() {
        return messageName;
    }

    private SourceFileLine sourceFileLine;

    @Override
    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
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
            requireNonNull(definition.sourceFileLine);
            requireNonNull(definition.qualifiedClassName);
            return definition;
        }

        private MessageDefinition definition = new MessageDefinition();

        public Builder messageName(String messageName) {
            definition.messageName = messageName;
            return this;
        }

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            definition.sourceFileLine = sourceFileLine;
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
}
