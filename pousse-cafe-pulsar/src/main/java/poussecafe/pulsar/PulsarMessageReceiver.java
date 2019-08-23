package poussecafe.pulsar;

import java.util.Objects;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import poussecafe.exception.PousseCafeException;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class PulsarMessageReceiver extends MessageReceiver {

    public static class Builder {

        public Builder messageBroker(MessageBroker messageBroker) {
            this.messageBroker = messageBroker;
            return this;
        }

        private MessageBroker messageBroker;

        public Builder consumerFactory(ConsumerFactory consumerFactory) {
            this.consumerFactory = consumerFactory;
            return this;
        }

        private ConsumerFactory consumerFactory;

        public PulsarMessageReceiver build() {
            Objects.requireNonNull(messageBroker);
            PulsarMessageReceiver receiver = new PulsarMessageReceiver(messageBroker);
            receiver.consumerFactory = consumerFactory;
            return receiver;
        }
    }

    private PulsarMessageReceiver(MessageBroker messageBroker) {
        super(messageBroker);
    }

    private ConsumerFactory consumerFactory;

    @Override
    protected void actuallyStartReceiving() {
        consumer = consumerFactory.buildConsumer();
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
                Message<String> message = null;
                try {
                    message = consumer.receive();
                    String stringPayload = message.getValue();
                    onMessage(new ReceivedMessage.Builder()
                            .payload(new OriginalAndMarshaledMessage.Builder()
                                    .marshaled(stringPayload)
                                    .original(messageAdapter.adaptSerializedMessage(stringPayload))
                                    .build())
                            .acker(ackRunnable(message))
                            .interrupter(this::stopReceiving)
                            .build());
                } catch (PulsarClientException e) {
                    logger.error("Error while consuming message, closing...", e);
                    break;
                } catch (Exception e) {
                    logger.error("Error while handling message ({}), continuing consumption and acking anyway...", e.getMessage());
                    logger.debug("Handling error stacktrace", e);
                    if(message != null) {
                        ack(message);
                    }
                }
            }
        };
    }

    private Runnable ackRunnable(Message<String> message) {
        return () -> ack(message);
    }

    private void ack(Message<String> message) {
        try {
            consumer.acknowledge(message);
        } catch (PulsarClientException e) {
            throw new PousseCafeException("Unable to ack message", e);
        }
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

    public void join() {
        try {
            receptionThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PousseCafeException("Cloud not join reception thread", e);
        }
    }
}
