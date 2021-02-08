package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class MessageImplementation
implements Serializable, HasClassNameConvention {

    private SourceLine sourceLine;

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    public Name messageDefinitionClassName() {
        return messageDefinitionClassName;
    }

    private Name messageDefinitionClassName;

    public List<String> messagingNames() {
        return Collections.unmodifiableList(messagingNames);
    }

    private List<String> messagingNames;

    @Override
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

    public boolean implementsMessage() {
        return implementsMessage;
    }

    private boolean implementsMessage;

    @Override
    public boolean validClassName() {
        return NamingConventions.isMessageImplementationName(className());
    }

    public static class Builder {

        public MessageImplementation build() {
            requireNonNull(implementation.sourceLine);
            requireNonNull(implementation.messageDefinitionClassName);
            requireNonNull(implementation.className);
            return implementation;
        }

        private MessageImplementation implementation = new MessageImplementation();

        public Builder sourceLine(SourceLine sourceLine) {
            implementation.sourceLine = sourceLine;
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

        public Builder implementsMessage(boolean implementsMessage) {
            implementation.implementsMessage = implementsMessage;
            return this;
        }
    }

    private MessageImplementation() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(concrete, other.concrete)
                .append(implementsMessage, other.implementsMessage)
                .append(messageDefinitionClassName, other.messageDefinitionClassName)
                .append(messagingNames, other.messagingNames)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(concrete)
                .append(implementsMessage)
                .append(messageDefinitionClassName)
                .append(messagingNames)
                .append(sourceLine)
                .build();
    }
}
