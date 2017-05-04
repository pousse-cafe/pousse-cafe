package poussecafe.messaging;

public class DefaultQueueSelector implements QueueSelector {

    @Override
    public Queue selectSource(Message message) {
        if (message instanceof Command) {
            return Queue.DEFAULT_COMMAND_QUEUE;
        } else {
            return Queue.DEFAULT_DOMAIN_EVENT_QUEUE;
        }
    }

}
