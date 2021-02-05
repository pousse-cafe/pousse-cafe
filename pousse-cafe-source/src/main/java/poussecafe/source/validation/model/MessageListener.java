package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class MessageListener implements Serializable {

    public SourceLine sourceLine() {
        return sourceLine;
    }

    private SourceLine sourceLine;

    public boolean isPublic() {
        return isPublic;
    }

    private boolean isPublic;

    public Optional<String> runnerQualifiedClassName() {
        return Optional.ofNullable(runnerQualifiedClassName);
    }

    private String runnerQualifiedClassName;

    public boolean returnsValue() {
        return returnsValue;
    }

    private boolean returnsValue;

    public Optional<Name> consumedMessageClass() {
        return Optional.ofNullable(consumedMessageClass);
    }

    private Name consumedMessageClass;

    public int parametersCount() {
        return parametersCount;
    }

    private int parametersCount;

    public MessageListenerContainerType containerType() {
        return containerType;
    }

    private MessageListenerContainerType containerType;

    public static class Builder {

        public MessageListener build() {
            requireNonNull(listener.sourceLine);
            requireNonNull(listener.containerType);
            return listener;
        }

        private MessageListener listener = new MessageListener();

        public Builder sourceLine(SourceLine sourceLine) {
            listener.sourceLine = sourceLine;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            listener.isPublic = isPublic;
            return this;
        }

        public Builder runnerQualifiedClassName(Optional<String> runnerQualifiedClassName) {
            listener.runnerQualifiedClassName = runnerQualifiedClassName.orElse(null);
            return this;
        }

        public Builder returnsValue(boolean returnsValue) {
            listener.returnsValue = returnsValue;
            return this;
        }

        public Builder consumedMessageClass(Optional<Name> consumedMessageQualifiedClassName) {
            listener.consumedMessageClass = consumedMessageQualifiedClassName.orElse(null);
            return this;
        }

        public Builder parametersCount(int parametersCount) {
            listener.parametersCount = parametersCount;
            return this;
        }

        public Builder containerType(MessageListenerContainerType containerType) {
            listener.containerType = containerType;
            return this;
        }
    }

    private MessageListener() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(consumedMessageClass, other.consumedMessageClass)
                .append(containerType, other.containerType)
                .append(isPublic, other.isPublic)
                .append(parametersCount, other.parametersCount)
                .append(returnsValue, other.returnsValue)
                .append(runnerQualifiedClassName, other.runnerQualifiedClassName)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(consumedMessageClass)
                .append(containerType)
                .append(isPublic)
                .append(parametersCount)
                .append(returnsValue)
                .append(runnerQualifiedClassName)
                .append(sourceLine)
                .build();
    }
}
