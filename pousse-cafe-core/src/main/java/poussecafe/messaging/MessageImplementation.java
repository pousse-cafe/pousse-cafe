package poussecafe.messaging;

import java.util.Objects;

public class MessageImplementation {

    public static class Builder {

        public Builder() {
            implementation = new MessageImplementation();
        }

        private MessageImplementation implementation;

        public Builder messageClass(Class<? extends Message> messageClass) {
            implementation.messageClass = messageClass;
            return this;
        }

        public Builder messageImplementationClass(Class<? extends Message> messageImplementationClass) {
            implementation.messageImplementationClass = messageImplementationClass;
            return this;
        }

        public Builder messaging(Messaging messaging) {
            implementation.messaging = messaging;
            return this;
        }

        public MessageImplementation build() {
            Objects.requireNonNull(implementation.messageClass);
            Objects.requireNonNull(implementation.messageImplementationClass);
            Objects.requireNonNull(implementation.messaging);
            return implementation;
        }
    }

    private MessageImplementation() {

    }

    private Class<? extends Message> messageClass;

    public Class<? extends Message> messageClass() {
        return messageClass;
    }

    private Class<? extends Message> messageImplementationClass;

    public Class<? extends Message> messageImplementationClass() {
        return messageImplementationClass;
    }

    private Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }
}
