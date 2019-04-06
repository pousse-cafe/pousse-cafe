package poussecafe.pulsar;

import java.util.Objects;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.runtime.MessageConsumer;

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
    public MessagingConnection connect(MessageConsumer messageConsumer) {
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageReceiver(new PulsarMessageReceiver.Builder()
                        .messageConsumer(messageConsumer)
                        .configuration(configuration)
                        .build())
                .messageSender(new PulsarMessageSender(configuration))
                .build();
    }

}
