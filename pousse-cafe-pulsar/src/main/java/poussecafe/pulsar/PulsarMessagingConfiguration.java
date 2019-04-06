package poussecafe.pulsar;

import java.util.Objects;

public class PulsarMessagingConfiguration {

    public static class Builder {

        private PulsarMessagingConfiguration configuration = new PulsarMessagingConfiguration();

        public Builder brokerUrl(String brokerUrl) {
            configuration.brokerUrl = brokerUrl;
            return this;
        }

        public Builder topic(String topic) {
            configuration.topic = topic;
            return this;
        }

        public Builder subscriptionName(String subscriptionName) {
            configuration.subscriptionName = subscriptionName;
            return this;
        }

        public PulsarMessagingConfiguration build() {
            Objects.requireNonNull(configuration.brokerUrl);
            Objects.requireNonNull(configuration.topic);
            Objects.requireNonNull(configuration.subscriptionName);
            return configuration;
        }
    }

    private PulsarMessagingConfiguration() {

    }

    private String brokerUrl;

    public String brokerUrl() {
        return brokerUrl;
    }

    private String topic;

    public String topic() {
        return topic;
    }

    private String subscriptionName;

    public String subscriptionName() {
        return subscriptionName;
    }
}
