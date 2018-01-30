package poussecafe.messaging;

public class InMemoryMessageQueueTest extends MessageReceiverTest {

    @Override
    protected MessageReceiver newMessageReceiver() {
        return new InMemoryMessageQueue();
    }

}
