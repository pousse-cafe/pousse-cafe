package poussecafe.messaging;

import java.util.Objects;

public class MessagingConnection {

    public static class Builder {

        private MessagingConnection connection = new MessagingConnection();

        public Builder messaging(Messaging messaging) {
            connection.messaging = messaging;
            return this;
        }

        public Builder messageSender(MessageSender messageSender) {
            connection.messageSender = messageSender;
            return this;
        }

        public Builder messageReceiver(MessageReceiver<?> messageReceiver) {
            connection.messageReceiver = messageReceiver;
            return this;
        }

        public MessagingConnection build() {
            Objects.requireNonNull(connection.messaging);
            Objects.requireNonNull(connection.messageSender);
            Objects.requireNonNull(connection.messageReceiver);
            return connection;
        }
    }

    private MessagingConnection() {

    }

    private Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }

    private MessageSender messageSender;

    public MessageSender messageSender() {
        return messageSender;
    }

    @SuppressWarnings("rawtypes")
    private MessageReceiver messageReceiver;

    @SuppressWarnings("rawtypes")
    public MessageReceiver messageReceiver() {
        return messageReceiver;
    }

    public void startReceiving() {
        messageReceiver.startReceiving();
    }

    public void stopReceiving() {
        messageReceiver.stopReceiving();
    }
}
