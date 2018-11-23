package poussecafe.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.MessageConsumer;

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
        messageConsumer.consumeMessage(message);
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void startReceiving() {
        if (started) {
            return;
        }
        actuallyStartReceiving();
        started = true;
    }

    private boolean started;

    protected abstract void actuallyStartReceiving();

    public boolean isStarted() {
        return started;
    }

    public void stopReceiving() {
        if(!started) {
            return;
        }
        actuallyStopReceiving();
        started = false;
    }

    protected abstract void actuallyStopReceiving();
}
