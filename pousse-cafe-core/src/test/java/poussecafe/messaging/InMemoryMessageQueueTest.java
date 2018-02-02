package poussecafe.messaging;

import poussecafe.messaging.internal.InternalMessageQueue;

public class InMemoryMessageQueueTest extends MessageReceiverTest {

    @Override
    protected MessageReceiver newMessageReceiver() {
        return new InternalMessageQueue();
    }

}
