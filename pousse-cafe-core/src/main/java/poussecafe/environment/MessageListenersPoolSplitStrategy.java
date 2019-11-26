package poussecafe.environment;

public interface MessageListenersPoolSplitStrategy {

    MessageListenersPool[] split(MessageListenersPool pool);
}
