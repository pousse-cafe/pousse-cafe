package poussecafe.pulsar;

import java.util.List;
import java.util.Objects;
import org.apache.pulsar.client.api.SubscriptionType;
import poussecafe.exception.PousseCafeException;

public class PulsarMessagingConfiguration {

    public static class Builder {

        private PulsarMessagingConfiguration configuration = new PulsarMessagingConfiguration();

        public Builder brokerUrl(String brokerUrl) {
            configuration.brokerUrl = brokerUrl;
            return this;
        }

        public Builder subscriptionTopics(List<String> subscriptionTopics) {
            configuration.subscriptionTopics = subscriptionTopics;
            return this;
        }

        public Builder subscriptionName(String subscriptionName) {
            configuration.subscriptionName = subscriptionName;
            return this;
        }

        public Builder defaultPublicationTopic(String defaultPublicationTopic) {
            configuration.defaultPublicationTopic = defaultPublicationTopic;
            return this;
        }

        public Builder publicationTopicChooser(PublicationTopicChooser publicationTopicChooser) {
            configuration.publicationTopicChooser = publicationTopicChooser;
            return this;
        }

        public Builder subscriptionType(SubscriptionType subscriptionType) {
            configuration.subscriptionType = subscriptionType;
            return this;
        }

        public PulsarMessagingConfiguration build() {
            Objects.requireNonNull(configuration.brokerUrl);
            Objects.requireNonNull(configuration.subscriptionTopics);
            if(configuration.subscriptionTopics.isEmpty()) {
                throw new PousseCafeException("At least one subscription topic is required");
            }
            Objects.requireNonNull(configuration.subscriptionName);
            Objects.requireNonNull(configuration.defaultPublicationTopic);
            Objects.requireNonNull(configuration.publicationTopicChooser);
            Objects.requireNonNull(configuration.subscriptionType);
            return configuration;
        }
    }

    private PulsarMessagingConfiguration() {

    }

    private String brokerUrl;

    public String brokerUrl() {
        return brokerUrl;
    }

    private List<String> subscriptionTopics;

    public List<String> topics() {
        return subscriptionTopics;
    }

    private String subscriptionName;

    public String subscriptionName() {
        return subscriptionName;
    }

    private String defaultPublicationTopic;

    public String defaultPublicationTopic() {
        return defaultPublicationTopic;
    }

    private PublicationTopicChooser publicationTopicChooser = new DefaultPublicationTopicChooser();

    public PublicationTopicChooser publicationTopicChooser() {
        return publicationTopicChooser;
    }

    public SubscriptionType subscriptionType() {
        return subscriptionType;
    }

    private SubscriptionType subscriptionType = SubscriptionType.Shared;
}
