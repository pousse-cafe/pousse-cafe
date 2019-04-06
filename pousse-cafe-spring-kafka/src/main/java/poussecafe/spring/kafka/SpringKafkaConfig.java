package poussecafe.spring.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
public class SpringKafkaConfig {

    @Bean
    public KafkaMessageListenerContainer<String, String> listenerContainer(
            ConsumerFactory<String, String> cf) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Value("${poussecafe.spring.kafka.topic:pousse-cafe}")
    public String topic;
}
