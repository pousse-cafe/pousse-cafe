package poussecafe.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.MessageConsumer;
import poussecafe.context.RawAndAdaptedMessage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;

public abstract class MessageReceiver {

    protected MessageReceiver(MessageAdapter messageAdapter,
            MessageConsumer messageConsumer) {
        checkThatValue(messageAdapter).notNull();
        this.messageAdapter = messageAdapter;

        checkThatValue(messageConsumer).notNull();
        this.messageConsumer = messageConsumer;
    }

    private MessageAdapter messageAdapter;

    private MessageConsumer messageConsumer;

    protected void onMessage(Object receivedMessage) {
        checkThat(value(receivedMessage).notNull().because("Received message cannot be null"));
        Message message = messageAdapter.adaptSerializedMessage(receivedMessage);
        messageConsumer.consumeMessage(new RawAndAdaptedMessage.Builder()
                .raw(receivedMessage)
                .adapted(message)
                .build());
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public synchronized void startReceiving() {
        if (started) {
            return;
        }
        actuallyStartReceiving();
        started = true;
    }

    private boolean started;

    protected abstract void actuallyStartReceiving();

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized void stopReceiving() {
        if(!started) {
            return;
        }
        actuallyStopReceiving();
        started = false;
    }

    protected abstract void actuallyStopReceiving();
}
