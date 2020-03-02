package poussecafe.messaging;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.OriginalAndMarshaledMessage;

/**
 * @param <E> The envelope type i.e. the class for messages for a given messaging technology. The payload is
 * extracted from that message and deserialized into an actual {@link Message}.
 */
public abstract class MessageReceiver<E> {

    protected MessageReceiver(MessageReceiverConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private MessageReceiverConfiguration configuration;

    protected void onMessage(E envelope) {
        Object stringPayload = extractPayload(envelope);

        Runnable acker = buildAcker(envelope);
        Message deserializedMessage;
        try {
            deserializedMessage = deserialize(stringPayload);
        } catch (Exception e) {
            if(configuration.failOnDeserializationError()) {
                throw e;
            } else {
                logger.debug("Could not deserialize payload", e);
                acker.run();
                return;
            }
        }

        onMessage(new ReceivedMessage.Builder()
                .payload(new OriginalAndMarshaledMessage.Builder()
                        .marshaled(stringPayload)
                        .original(deserializedMessage)
                        .build())
                .acker(acker)
                .interrupter(this::interruptReception)
                .build());
    }

    protected abstract Object extractPayload(E envelope);

    protected abstract Message deserialize(Object payload);

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private void onMessage(ReceivedMessage receivedMessage) {
        Objects.requireNonNull(receivedMessage);
        configuration.messageBroker().dispatch(receivedMessage);
    }

    protected abstract Runnable buildAcker(E envelope);

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
