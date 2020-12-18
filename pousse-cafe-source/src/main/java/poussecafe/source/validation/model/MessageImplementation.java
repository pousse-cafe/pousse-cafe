package poussecafe.source.validation.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class MessageImplementation {

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    public Optional<String> messageDefinitionQualifiedClassName() {
        return messageDefinitionQualifiedClassName;
    }

    private Optional<String> messageDefinitionQualifiedClassName = Optional.empty();

    public List<String> messagingNames() {
        return Collections.unmodifiableList(messagingNames);
    }

    private List<String> messagingNames;

    public static class Builder {

        public MessageImplementation build() {
            requireNonNull(implementation.sourceFileLine);
            requireNonNull(implementation.messageDefinitionQualifiedClassName);
            return implementation;
        }

        private MessageImplementation implementation = new MessageImplementation();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            implementation.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder messageDefinitionQualifiedClassName(Optional<String> messageDefinitionQualifiedClassName) {
            implementation.messageDefinitionQualifiedClassName = messageDefinitionQualifiedClassName;
            return this;
        }

        public Builder messagingNames(List<String> messagingNames) {
            implementation.messagingNames = messagingNames;
            return this;
        }
    }

    private MessageImplementation() {

    }
}
