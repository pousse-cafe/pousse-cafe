package poussecafe.pulsar;

import java.util.Objects;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import poussecafe.exception.PousseCafeException;

public class ConsumerFactory {

    public ConsumerFactory(PulsarMessagingConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private PulsarMessagingConfiguration configuration;

    public Consumer<String> buildConsumer() {
        try {
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(configuration.brokerUrl())
                    .build();
            return client.newConsumer(Schema.STRING)
                    .topics(configuration.topics())
                    .subscriptionType(configuration.subscriptionType())
                    .subscriptionName(configuration.subscriptionName())
                    .subscribe();
        } catch (PulsarClientException e) {
            throw new PousseCafeException("Unable to connect to Pulsar broker", e);
        }
    }
}
