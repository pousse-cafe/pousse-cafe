package poussecafe.spring.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;

public class SpringKafkaMessaging extends Messaging {

    public static final String NAME = "spring-kafka";

    @Override
    public String name() {
        return NAME;
    }

    public static SpringKafkaMessaging instance() {
        synchronized(SpringKafkaMessaging.class) {
            if(instance == null) {
                instance = new SpringKafkaMessaging();
            }
            return instance;
        }
    }

    private static SpringKafkaMessaging instance;

    private SpringKafkaMessaging() {

    }

    @Override
    public MessagingConnection connect(MessageBroker messageBroker) {
        synchronized(SpringKafkaMessaging.class) {
            if(messageSenderAndReceiverFactory == null) {
                throw new PousseCafeException("Cannot connect, messaging not yet configured");
            }
            logger.info("Connecting Spring Kafka messaging to message broker {}", messageBroker);
            return new MessagingConnection.Builder()
                    .messaging(this)
                    .messageSender(messageSenderAndReceiverFactory.buildMessageSender())
                    .messageReceiver(messageSenderAndReceiverFactory.buildMessageReceiver(messageBroker))
                    .build();
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

    static void setFactory(MessageSenderAndReceiverFactory messageSenderAndReceiverFactory) {
        synchronized(SpringKafkaMessaging.class) {
            instance.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
        }
    }
}
