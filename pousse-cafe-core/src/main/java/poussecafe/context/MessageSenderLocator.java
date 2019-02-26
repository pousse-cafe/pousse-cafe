package poussecafe.context;

import java.util.List;
import poussecafe.environment.Environment;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;

public class MessageSenderLocator {

    MessageSenderLocator(List<MessagingConnection> connections) {
        this.connections = connections;
    }

    private List<MessagingConnection> connections;

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
}
