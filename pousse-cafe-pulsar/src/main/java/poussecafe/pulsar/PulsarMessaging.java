package poussecafe.pulsar;

import java.util.Objects;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;

public class PulsarMessaging extends Messaging {

    public PulsarMessaging(PulsarMessagingConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private PulsarMessagingConfiguration configuration;

    public static final String NAME = "pulsar";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public MessagingConnection connect(MessageBroker messageBroker) {
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageReceiver(new PulsarMessageReceiver.Builder()
                        .messageConsumer(messageBroker)
                        .configuration(configuration)
                        .build())
                .messageSender(new PulsarMessageSender(configuration))
                .build();
    }
}
