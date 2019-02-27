package poussecafe.runtime;

import java.util.List;
import java.util.Objects;
import poussecafe.environment.Environment;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;

public class MessageSenderLocator {

    public static class Builder {

        private MessageSenderLocator locator = new MessageSenderLocator();

        public Builder environment(Environment environment) {
            locator.environment = environment;
            return this;
        }

        public Builder connections(List<MessagingConnection> connections) {
            locator.connections = connections;
            return this;
        }

        public MessageSenderLocator build() {
            Objects.requireNonNull(locator.environment);
            Objects.requireNonNull(locator.connections);
            return locator;
        }
    }

    private MessageSenderLocator() {

    }

    public MessageSender locate(Class<? extends Message> messageClassOrImplementation) {
        Class<? extends Message> messageClass = environment.definedMessageClass(messageClassOrImplementation);
        Messaging messaging = environment.messagingOf(messageClass);
        return locateConnection(messaging).messageSender();
    }

    private Environment environment;

    private MessagingConnection locateConnection(Messaging messaging) {
        return connections.stream()
                .filter(connection -> connection.messaging() == messaging)
                .findFirst()
                .orElseThrow(() -> new PousseCafeException("No connection found for messaging " + messaging));
    }

    private List<MessagingConnection> connections;
}
