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
    protected Object serializedMessage(Message message) {
        return new SerializingMessageAdapter().adaptMessage(message);
    }
}
