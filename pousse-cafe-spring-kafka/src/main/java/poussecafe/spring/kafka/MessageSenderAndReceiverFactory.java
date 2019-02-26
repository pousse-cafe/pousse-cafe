package poussecafe.spring.kafka;

import java.util.HashSet;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.runtime.MessageConsumer;

@Component
public class MessageSenderAndReceiverFactory implements InitializingBean, MessageListener<String, String> {

    @Override
    public void afterPropertiesSet()
            throws Exception {
        logger.info("Configuring Spring Kafka messaging");
        SpringKafkaMessaging.setFactory(this);

        listenerContainer.setupMessageListener(this);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessageSender buildMessageSender() {
        return new KafkaMessageSender.Builder()
                .kafkaTemplate(template)
                .topic(listenerContainer.getContainerProperties().getTopics()[0])
                .build();
    }

    @Autowired
    private KafkaMessageListenerContainer<String, String> listenerContainer;

    @Autowired
    private KafkaTemplate<String, String> template;

    void startListenerContainer() {
        listenerContainer.start();
    }

    public MessageReceiver buildMessageReceiver(MessageConsumer messageConsumer) {
        return new KafkaMessageReceiver.Builder()
                .messageConsumer(messageConsumer)
                .messageSenderAndReceiverFactory(this)
                .build();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> consumerRecord) {
        String payload = consumerRecord.value();
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
            listenerContainer.stop();
        }
    }
}
