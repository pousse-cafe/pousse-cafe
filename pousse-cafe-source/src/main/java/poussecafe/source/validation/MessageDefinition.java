package poussecafe.source.validation;

import static java.util.Objects.requireNonNull;

public class MessageDefinition {

    private String messageName;

    public String messageName() {
        return messageName;
    }

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    public String qualifiedClassName() {
        return qualifiedClassName;
    }

    private String qualifiedClassName;

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
    }

    private MessageDefinition() {

    }
}
