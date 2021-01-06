package poussecafe.source.validation.model;

import java.util.Collections;
import java.util.List;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class MessageImplementation {

    private SourceFileLine sourceFileLine;

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    public Name messageDefinitionClassName() {
        return messageDefinitionClassName;
    }

    private Name messageDefinitionClassName;

    public List<String> messagingNames() {
        return Collections.unmodifiableList(messagingNames);
    }

    private List<String> messagingNames;

    public Name className() {
        return className;
    }

    private Name className;

    public boolean isAutoImplementation() {
        return messageDefinitionClassName.equals(className);
    }

    public boolean isConcrete() {
        return concrete;
    }

    private boolean concrete;

    public boolean isMessage() {
        return message;
    }

    private boolean message;

    public static class Builder {

        public MessageImplementation build() {
            requireNonNull(implementation.sourceFileLine);
            requireNonNull(implementation.messageDefinitionClassName);
            requireNonNull(implementation.className);
            return implementation;
        }

        private MessageImplementation implementation = new MessageImplementation();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            implementation.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder messageDefinitionClassName(Name messageDefinitionClassName) {
            implementation.messageDefinitionClassName = messageDefinitionClassName;
            return this;
        }

        public Builder messagingNames(List<String> messagingNames) {
            implementation.messagingNames = messagingNames;
            return this;
        }

        public Builder className(Name className) {
            implementation.className = className;
            return this;
        }

        public Builder concrete(boolean concrete) {
            implementation.concrete = concrete;
            return this;
        }

        public Builder message(boolean message) {
            implementation.message = message;
            return this;
        }
    }

    private MessageImplementation() {

    }
}
