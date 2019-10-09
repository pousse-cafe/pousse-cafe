package poussecafe.test;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;

public class DummyMessageReceiver extends MessageReceiver<Object> {

    protected DummyMessageReceiver(MessageBroker messageBroker) {
        super(messageBroker);
    }

    @Override
    protected void actuallyStartReceiving() {
        // Do nothing
    }

    @Override
    protected void actuallyStopReceiving() {
        // Do nothing
    }

    @Override
    protected Object extractPayload(Object envelope) {
        return envelope;
    }

    @Override
    protected Message deserialize(Object payload) {
        return (Message) payload;
    }

    @Override
    protected Runnable buildAcker(Object envelope) {
        return () -> {};
    }
}
