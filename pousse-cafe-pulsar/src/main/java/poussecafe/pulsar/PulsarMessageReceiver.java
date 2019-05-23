package poussecafe.pulsar;

import java.util.Objects;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.runtime.MessageConsumer;

public class PulsarMessageReceiver extends MessageReceiver {

    public static class Builder {

        public Builder messageConsumer(MessageConsumer messageConsumer) {
            this.messageConsumer = messageConsumer;
            return this;
        }

        private MessageConsumer messageConsumer;

        public Builder configuration(PulsarMessagingConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        private PulsarMessagingConfiguration configuration;

        public PulsarMessageReceiver build() {
            Objects.requireNonNull(messageConsumer);
            PulsarMessageReceiver receiver = new PulsarMessageReceiver(messageConsumer);
            receiver.configuration = configuration;
            return receiver;
        }
    }

    private PulsarMessageReceiver(MessageConsumer messageConsumer) {
        super(new JacksonMessageAdapter(), messageConsumer);
    }

    private PulsarMessagingConfiguration configuration;

    @Override
    protected void actuallyStartReceiving() {
        try {
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(configuration.brokerUrl())
                    .build();
            consumer = client.newConsumer(Schema.STRING)
                    .topics(configuration.topics())
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionName(configuration.subscriptionName())
                    .subscribe();
        } catch (PulsarClientException e) {
            throw new PousseCafeException("Unable to connect to Pulsar broker", e);
        }
        startReceptionThread();
    }

    private Consumer<String> consumer;

    private void startReceptionThread() {
        receptionThread = new Thread(receptionRunnable());
        receptionThread.setDaemon(true);
        receptionThread.start();
    }

    private Thread receptionThread;

    private Runnable receptionRunnable() {
        return () -> {
            Message<String> message = null;
            while(true) {
                try {
                    message = consumer.receive();
                    onMessage(message.getValue());
                } catch (Exception e) {
                    logger.error("Error while handling message ({}), continuing consumption anyway...", e.getMessage());
                    logger.debug("Handling error stacktrace", e);
                } finally {
                    if(message != null) {
                        try {
                            consumer.acknowledge(message);
                        } catch (PulsarClientException e) {
                            logger.error("Unable to acknowledge message");
                        }
                        message = null;
                    }
                }
            }
        };
    }

    private void closeIfConnected() {
        if(consumer.isConnected()) {
            try {
                consumer.close();
            } catch (PulsarClientException e) {
                logger.warn("Error while closing consumer", e);
            }
        }
    }

    @Override
    protected void actuallyStopReceiving() {
        closeIfConnected();
        receptionThread.interrupt();
    }
}
