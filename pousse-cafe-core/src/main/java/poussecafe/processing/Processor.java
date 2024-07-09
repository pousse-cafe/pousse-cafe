package poussecafe.processing;

import java.util.List;

public interface Processor {

    void start();

    void submit(ReceivedMessage receivedMessage, List<MessageListenersGroup> groups);

    void stop();

}
