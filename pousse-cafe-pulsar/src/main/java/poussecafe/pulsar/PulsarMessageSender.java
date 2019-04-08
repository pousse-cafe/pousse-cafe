package poussecafe.pulsar;

import java.util.Objects;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageSender;

public class PulsarMessageSender extends MessageSender {

    protected PulsarMessageSender(PulsarMessagingConfiguration configuration) {
        super(new JacksonMessageAdapter());
        configuration(configuration);
        createProducer();
    }

    private void configuration(PulsarMessagingConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private PulsarMessagingConfiguration configuration;

    private void createProducer() {
        try {
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(configuration.brokerUrl())
                    .build();
            producer = client.newProducer(Schema.STRING)
                    .topic(configuration.defaultPublicationTopic())
                    .create();
        } catch (PulsarClientException e) {
            throw new PousseCafeException("Unable to connect to Pulsar broker", e);
        }
    }

    private Producer<String> producer;

    @Override
    protected void sendMarshalledMessage(Object marshalledMessage) {
        try {
            producer.send((String) marshalledMessage);
        } catch (PulsarClientException e) {
            throw new PousseCafeException("Unable to send message to Pulsar broker", e);
        }
    }
}
