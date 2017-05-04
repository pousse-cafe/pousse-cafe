package poussecafe.messaging;

import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.InMemoryMessageQueue;
import poussecafe.messaging.Queue;

public class InMemoryMessageQueueTest extends MessageReceiverTest {

    @Override
    protected MessageReceiver newMessageReceiver(Queue source) {
        return new InMemoryMessageQueue(source);
    }

}
