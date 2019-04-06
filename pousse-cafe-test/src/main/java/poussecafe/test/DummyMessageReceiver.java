package poussecafe.test;

import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.TransparentMessageAdapter;
import poussecafe.runtime.MessageConsumer;

public class DummyMessageReceiver extends MessageReceiver {

    protected DummyMessageReceiver(MessageConsumer messageConsumer) {
        super(new TransparentMessageAdapter(), messageConsumer);
    }

    @Override
    protected void actuallyStartReceiving() {
        // Do nothing
    }

    @Override
    protected void actuallyStopReceiving() {
        // Do nothing
    }
}
