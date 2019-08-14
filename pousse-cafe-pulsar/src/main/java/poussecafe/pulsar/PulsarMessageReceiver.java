package poussecafe.pulsar;

import java.util.Objects;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class PulsarMessageReceiver extends MessageReceiver {

    public static class Builder {

        public Builder messageConsumer(MessageBroker messageBroker) {
            this.messageBroker = messageBroker;
            return this;
        }

        private MessageBroker messageBroker;

        public Builder configuration(PulsarMessagingConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        private PulsarMessagingConfiguration configuration;

        public PulsarMessageReceiver build() {
            Objects.requireNonNull(messageBroker);
            PulsarMessageReceiver receiver = new PulsarMessageReceiver(messageBroker);
            receiver.configuration = configuration;
            return receiver;
        }
    }

    private PulsarMessageReceiver(MessageBroker messageBroker) {
        super(messageBroker);
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
                    .subscriptionType(configuration.subscriptionType())
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
            while(true) {
                try {
                    Message<String> message = consumer.receive();
                    String stringPayload = message.getValue();
                    onMessage(new ReceivedMessage.Builder()
                            .payload(new OriginalAndMarshaledMessage.Builder()
                                    .marshaled(stringPayload)
                                    .original(messageAdapter.adaptSerializedMessage(stringPayload))
                                    .build())
                            .acker(ack(message))
                            .build());
                } catch (Exception e) {
                    logger.error("Error while handling message ({}), continuing consumption anyway...", e.getMessage());
                    logger.debug("Handling error stacktrace", e);
                }
            }
        };
    }

    private Runnable ack(Message<String> message) {
        return () -> {
            try {
                consumer.acknowledge(message);
            } catch (PulsarClientException e) {
                throw new PousseCafeException("Unable to ack message", e);
            }
        };
    }

    private JacksonMessageAdapter messageAdapter = new JacksonMessageAdapter();

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
