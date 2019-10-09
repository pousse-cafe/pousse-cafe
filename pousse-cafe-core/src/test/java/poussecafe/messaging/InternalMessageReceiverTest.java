package poussecafe.messaging;

import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.messaging.internal.SerializingMessageAdapter;

public class InternalMessageReceiverTest extends MessageReceiverTest {

    @Override
    protected Messaging messaging() {
        return InternalMessaging.instance();
    }

    @Override
    protected Message message() {
        return new TestMessage();
    }

    @Override
    protected Object envelope(Message message) {
        return new SerializingMessageAdapter().adaptMessage(message);
    }

    @Override
    protected Object serializedMessage(Object envelope) {
        return envelope;
    }
}
