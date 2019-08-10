package poussecafe.messaging;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.FailFastException;

public abstract class MessageReceiver {

    protected MessageReceiver(MessageBroker messageBroker) {
        Objects.requireNonNull(messageBroker);
        this.messageBroker = messageBroker;
    }

    private MessageBroker messageBroker;

    protected void onMessage(ReceivedMessage receivedMessage) {
        Objects.requireNonNull(receivedMessage);
        try {
            messageBroker.dispatch(receivedMessage);
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
