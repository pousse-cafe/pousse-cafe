package poussecafe.messaging;

import poussecafe.messaging.internal.SerializingMessageAdapter;
import poussecafe.messaging.sync.SynchronousMessaging;

public class SynchronousMessagingTest extends MessagingTest {

    @Override
    protected Messaging messaging() {
        return SynchronousMessaging.instance();
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
