package poussecafe.processing;

public interface ProcessingThreadSelector {

    int selectFor(MessageListenersGroup group);
}
