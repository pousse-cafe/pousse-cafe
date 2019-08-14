package poussecafe.spring.pulsar;

import java.util.List;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poussecafe.pulsar.PublicationTopicChooser;
import poussecafe.pulsar.PulsarMessaging;
import poussecafe.pulsar.PulsarMessagingConfiguration;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Configuration
public class SpringPulsarConfig {

    @Bean
    public PulsarMessaging pulsarMessaging(
            @Value("${poussecafe.spring.pulsar.broker:pulsar://localhost:6650}") String brokerUrl,
            @Value("${poussecafe.spring.pulsar.subscriptionTopics:pousse-cafe}") String subscriptionTopics,
            @Value("${poussecafe.spring.pulsar.subscriptionName:pousse-cafe}") String subscriptionName,
            @Value("${poussecafe.spring.pulsar.defaultPublicationTopic:pousse-cafe}") String defaultPublicationTopic,
            @Value("${poussecafe.spring.pulsar.subscriptionType:Shared}") String subscriptionType,
            @Autowired(required = false) PublicationTopicChooser publicationTopicChooser) {
        PulsarMessagingConfiguration.Builder configurationBuilder = new PulsarMessagingConfiguration.Builder()
                .brokerUrl(brokerUrl)
                .subscriptionTopics(parseSubscriptionTopics(subscriptionTopics))
                .subscriptionName(subscriptionName)
                .defaultPublicationTopic(defaultPublicationTopic)
                .subscriptionType(SubscriptionType.valueOf(subscriptionType));
        if(publicationTopicChooser != null) {
            configurationBuilder.publicationTopicChooser(publicationTopicChooser);
        }
        return new PulsarMessaging(configurationBuilder.build());
    }

    private List<String> parseSubscriptionTopics(String subscriptionTopics) {
        return asList(subscriptionTopics.split(",")).stream()
                .map(String::trim)
                .collect(toList());
    }
}
