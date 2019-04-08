package poussecafe.messaging;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumer;
import poussecafe.runtime.OriginalAndMarshaledMessage;

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
        try {
            messageConsumer.consumeMessage(new OriginalAndMarshaledMessage.Builder()
                    .marshaled(receivedMessage)
                    .original(message)
                    .build());
        } catch (FailFastException e) {
            interruptReception();
        }
    }

    protected synchronized void interruptReception() {
        if(!started) {
            return;
        }
        started = false;
        actuallyInterruptReception();
    }

    protected void actuallyInterruptReception() {
        throw new UnsupportedOperationException();
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public synchronized void startReceiving() {
        if (started) {
            return;
        }
        started = true;
        actuallyStartReceiving();
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
        started = false;
        actuallyStopReceiving();
    }

    protected abstract void actuallyStopReceiving();
}
