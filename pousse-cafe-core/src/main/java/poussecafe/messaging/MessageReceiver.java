package poussecafe.messaging;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.runtime.MessageConsumer;
import poussecafe.runtime.RawAndAdaptedMessage;

public abstract class MessageReceiver {

    protected MessageReceiver(MessageAdapter messageAdapter,
            MessageConsumer messageConsumer) {
        Objects.requireNonNull(messageAdapter);
        this.messageAdapter = messageAdapter;

        Objects.requireNonNull(messageConsumer);
        this.messageConsumer = messageConsumer;
    }

    private MessageAdapter messageAdapter;

    private MessageConsumer messageConsumer;

    protected void onMessage(Object receivedMessage) {
        Objects.requireNonNull(receivedMessage);
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
