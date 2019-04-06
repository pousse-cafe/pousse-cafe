package poussecafe.spring.pulsar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poussecafe.pulsar.PulsarMessaging;
import poussecafe.pulsar.PulsarMessagingConfiguration;

@Configuration
public class SpringPulsarConfig {

    @Bean
    public PulsarMessaging pulsarMessaging(
            @Value("${poussecafe.spring.pulsar.broker:pulsar://localhost:6650}") String brokerUrl,
            @Value("${poussecafe.spring.pulsar.topic:pousse-cafe}") String topic,
            @Value("${poussecafe.spring.pulsar.subscriptionName:pousse-cafe}") String subscriptionName) {
        PulsarMessagingConfiguration configuration = new PulsarMessagingConfiguration.Builder()
                .brokerUrl(brokerUrl)
                .topic(topic)
                .subscriptionName(subscriptionName)
                .build();
        return new PulsarMessaging(configuration);
    }
}
