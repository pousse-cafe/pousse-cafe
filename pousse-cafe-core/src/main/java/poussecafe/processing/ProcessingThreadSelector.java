package poussecafe.processing;

public interface ProcessingThreadSelector {

    int selectFor(MessageListenersGroup group);

    void unselect(int threadId, MessageListenersGroup messageListenerGroup);
}
