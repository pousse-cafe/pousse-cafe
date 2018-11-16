package poussecafe.messaging;

import poussecafe.messaging.internal.InternalMessaging;

public class InternalMessageReceiverTest extends MessageReceiverTest {

    @Override
    protected Messaging messaging() {
        return InternalMessaging.instance();
    }
}
