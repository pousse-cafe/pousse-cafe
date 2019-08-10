package poussecafe.spring.kafka;

import java.util.Objects;
import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;

public class KafkaMessageReceiver extends MessageReceiver {

    public static class Builder {

        public Builder messageBroker(MessageBroker messageBroker) {
            this.messageBroker = messageBroker;
            return this;
        }

        private MessageBroker messageBroker;

        public Builder messageSenderAndReceiverFactory(MessageSenderAndReceiverFactory messageSenderAndReceiverFactory) {
            this.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return this;
        }

        private MessageSenderAndReceiverFactory messageSenderAndReceiverFactory;

        public KafkaMessageReceiver build() {
            Objects.requireNonNull(messageBroker);
            Objects.requireNonNull(messageSenderAndReceiverFactory);

            KafkaMessageReceiver receiver = new KafkaMessageReceiver(messageBroker);
            receiver.messageSenderAndReceiverFactory = messageSenderAndReceiverFactory;
            return receiver;
        }
    }

    private KafkaMessageReceiver(MessageBroker messageBroker) {
        super(messageBroker);
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

    void consume(ReceivedMessage receivedMessage) {
        onMessage(receivedMessage);
    }
}
