package poussecafe.test;

import poussecafe.messaging.MessageSender;
import poussecafe.messaging.TransparentMessageAdapter;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class DummyMessageSender extends MessageSender {

    public DummyMessageSender() {
        super(new TransparentMessageAdapter());
    }

    @Override
    protected void sendMarshalledMessage(OriginalAndMarshaledMessage marshalledMessage) {
        // Do nothing
    }
}
