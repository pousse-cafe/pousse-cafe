package poussecafe.messaging;

import java.util.Objects;
import poussecafe.processing.MessageBroker;

public class MessageReceiverConfiguration {

    public static class Builder {

        private MessageReceiverConfiguration configuration = new MessageReceiverConfiguration();

        public Builder messageBroker(MessageBroker messageBroker) {
            configuration.messageBroker = messageBroker;
            return this;
        }

        public Builder failOnDeserializationError(boolean failOnDeserializationError) {
            configuration.failOnDeserializationError = failOnDeserializationError;
            return this;
        }

        public MessageReceiverConfiguration build() {
            Objects.requireNonNull(configuration.messageBroker);
            return configuration;
        }
    }

    private MessageReceiverConfiguration() {

    }

    private MessageBroker messageBroker;

    public MessageBroker messageBroker() {
        return messageBroker;
    }

    private boolean failOnDeserializationError = true;

    public boolean failOnDeserializationError() {
        return failOnDeserializationError;
    }
}
