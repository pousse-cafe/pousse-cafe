package poussecafe.test;

import poussecafe.messaging.MessageReceiver;
import poussecafe.processing.MessageBroker;

public class DummyMessageReceiver extends MessageReceiver {

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
}
