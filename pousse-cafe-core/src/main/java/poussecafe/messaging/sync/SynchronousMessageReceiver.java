package poussecafe.messaging.sync;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageReceiverConfiguration;
import poussecafe.messaging.internal.SerializingMessageAdapter;

public class SynchronousMessageReceiver extends MessageReceiver<Object> {

    protected SynchronousMessageReceiver(MessageReceiverConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected Object extractPayload(Object envelope) {
        return envelope;
    }

    @Override
    protected Message deserialize(Object payload) {
        return messageAdapter.adaptSerializedMessage(payload);
    }

    private MessageAdapter messageAdapter = new SerializingMessageAdapter();

    @Override
    protected Runnable buildAcker(Object envelope) {
        return () -> {};
    }

    @Override
    protected void actuallyStartReceiving() {
        // NOOP
    }

    @Override
    protected void actuallyStopReceiving() {
        // NOOP
    }

    public void processMessage(Object marshaled) {
        onMessage(marshaled);
    }
}
