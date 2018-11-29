package poussecafe.spring.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.MessageConsumer;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;

public class DisconnectedSpringKafkaMessaging extends Messaging {

    public static final String NAME = SpringKafkaMessaging.NAME;

    @Override
    public String name() {
        return NAME;
    }

    public static DisconnectedSpringKafkaMessaging instance() {
        synchronized(DisconnectedSpringKafkaMessaging.class) {
            if(instance == null) {
                instance = new DisconnectedSpringKafkaMessaging();
            }
            return instance;
        }
    }

    private static DisconnectedSpringKafkaMessaging instance;

    private DisconnectedSpringKafkaMessaging() {

    }

    @Override
    public MessagingConnection connect(MessageConsumer messageConsumer) {
        logger.info("Connecting Spring Kafka messaging to message consumer {}", messageConsumer);
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageSender(noOpMessageSender())
                .messageReceiver(noOpMessageReceiver(messageConsumer))
                .build();
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MessageSender noOpMessageSender() {
        return new MessageSender(new JacksonMessageAdapter()) {
            @Override
            protected void sendMarshalledMessage(Object marshalledMessage) {
                // SKIP
            }
        };
    }

    private MessageReceiver noOpMessageReceiver(MessageConsumer messageConsumer) {
        return new MessageReceiver(new JacksonMessageAdapter(), messageConsumer) {
            @Override
            protected void actuallyStartReceiving() {
                // SKIP
            }

            @Override
            protected void actuallyStopReceiving() {
                // SKIP
            }
        };
    }
}
