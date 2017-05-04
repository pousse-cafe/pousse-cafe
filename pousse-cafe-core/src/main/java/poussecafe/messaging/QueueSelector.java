package poussecafe.messaging;

public interface QueueSelector {

    Queue selectSource(Message message);

}
