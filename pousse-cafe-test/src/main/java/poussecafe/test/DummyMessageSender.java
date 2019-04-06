package poussecafe.test;

import poussecafe.messaging.MessageSender;
import poussecafe.messaging.TransparentMessageAdapter;

public class DummyMessageSender extends MessageSender {

    public DummyMessageSender() {
        super(new TransparentMessageAdapter());
    }

    @Override
    protected void sendMarshalledMessage(Object marshalledMessage) {
        // Do nothing
    }
}
