package poussecafe.source.validation.model;

import java.util.Optional;
import poussecafe.source.analysis.ResolvedClass;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.SourceFileLine;

import static java.util.Objects.requireNonNull;

public class MessageListener {

    public SourceFileLine sourceFileLine() {
        return sourceFileLine;
    }

    private SourceFileLine sourceFileLine;

    public boolean isPublic() {
        return isPublic;
    }

    private boolean isPublic;

    public Optional<String> runnerQualifiedClassName() {
        return runnerQualifiedClassName;
    }

    private Optional<String> runnerQualifiedClassName = Optional.empty();

    public boolean returnsValue() {
        return returnsValue;
    }

    private boolean returnsValue;

    public Optional<ResolvedClass> consumedMessageClass() {
        return consumedMessageClass;
    }

    private Optional<ResolvedClass> consumedMessageClass = Optional.empty();

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
            requireNonNull(listener.sourceFileLine);
            requireNonNull(listener.runnerQualifiedClassName);
            requireNonNull(listener.consumedMessageClass);
            requireNonNull(listener.containerType);
            return listener;
        }

        private MessageListener listener = new MessageListener();

        public Builder sourceFileLine(SourceFileLine sourceFileLine) {
            listener.sourceFileLine = sourceFileLine;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            listener.isPublic = isPublic;
            return this;
        }

        public Builder runnerQualifiedClassName(Optional<String> runnerQualifiedClassName) {
            listener.runnerQualifiedClassName = runnerQualifiedClassName;
            return this;
        }

        public Builder returnsValue(boolean returnsValue) {
            listener.returnsValue = returnsValue;
            return this;
        }

        public Builder consumedMessageClass(Optional<ResolvedClass> consumedMessageQualifiedClassName) {
            listener.consumedMessageClass = consumedMessageQualifiedClassName;
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
}
