package poussecafe.spring.kafka;

import java.util.HashSet;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import poussecafe.context.MessageConsumer;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;

@Component
public class MessageSenderAndReceiverFactory implements InitializingBean {

    @Override
    public void afterPropertiesSet()
            throws Exception {
        logger.info("Configuring Spring Kafka messaging");
        SpringKafkaMessaging.setFactory(this);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessageSender buildMessageSender() {
        return new KafkaMessageSender.Builder()
                .kafkaTemplate(template)
                .topic(DEFAULT_TOPIC)
                .build();
    }

    public static final String DEFAULT_TOPIC = "pousse-cafe";

    @Autowired
    private KafkaTemplate<String, String> template;

    void startListenerContainer() {
        registry.getListenerContainer(CONTAINER_ID).start();
    }

    public static final String CONTAINER_ID = "pousse-cafe";

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    public MessageReceiver buildMessageReceiver(MessageConsumer messageConsumer) {
        return new KafkaMessageReceiver.Builder()
                .messageConsumer(messageConsumer)
                .messageSenderAndReceiverFactory(this)
                .build();
    }

    @KafkaListener(id = CONTAINER_ID, topics = DEFAULT_TOPIC, autoStartup = "false")
    private synchronized void globalReceiver(ConsumerRecord<?, ?> consumerRecord) {
        String payload = (String) consumerRecord.value();
        for(KafkaMessageReceiver receiver : receivers) {
            receiver.consume(payload);
        }
    }

    synchronized void registerReceiver(KafkaMessageReceiver kafkaMessageReceiver) {
        receivers.add(kafkaMessageReceiver);
    }

    private Set<KafkaMessageReceiver> receivers = new HashSet<>();

    synchronized void deregisterReceiver(KafkaMessageReceiver kafkaMessageReceiver) {
        receivers.remove(kafkaMessageReceiver);
        if(receivers.isEmpty()) {
            registry.getListenerContainer(CONTAINER_ID).stop();
        }
    }
}
