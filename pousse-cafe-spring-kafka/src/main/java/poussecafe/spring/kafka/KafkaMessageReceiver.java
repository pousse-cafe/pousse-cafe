package poussecafe.spring.kafka;

import java.util.Objects;
import poussecafe.context.MessageConsumer;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageReceiver;

public class KafkaMessageReceiver extends MessageReceiver {

    public static class Builder {

        public Builder messageConsumer(MessageConsumer messageConsumer) {
            this.messageConsumer = messageConsumer;
            return this;
        }

        private MessageConsumer messageConsumer;

        public Builder messageSenderAndReceiverFactory(MessageSenderAndReceiverFactory messageSenderAndReceiverFactory) {
            this.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return this;
        }

        private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

        public KafkaMessageReceiver build() {
            Objects.requireNonNull(messageConsumer);
            Objects.requireNonNull(messageSenderAndReceiverFactory);

            KafkaMessageReceiver receiver = new KafkaMessageReceiver(messageConsumer);
            receiver.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return receiver;
        }
    }

    private KafkaMessageReceiver(MessageConsumer messageConsumer) {
        super(new JacksonMessageAdapter(), messageConsumer);
    }

    private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

    @Override
    protected void actuallyStartReceiving() {
        messageSenderAndReceiverFactory.registerReceiver(this);
        messageSenderAndReceiverFactory.startListenerContainer();
    }

    @Override
    protected void actuallyStopReceiving() {
        messageSenderAndReceiverFactory.deregisterReceiver(this);
    }

    void consume(String payload) {
        onMessage(payload);
    }
}
