package poussecafe.environment;


public class MessageListeners {

    public void include(MessageListener listener) {
        MessageListenerType priority = listener.type();
        if(priority == MessageListenerType.FACTORY) {
            if(factoryListener == null) {
                factoryListener = listener;
            } else {
                throw new IllegalArgumentException("There is already a factory listener in " + listener.aggregateRootClass().orElseThrow() + " for message " + listener.consumedMessageClass().getName());
            }
        } else if(priority == MessageListenerType.AGGREGATE) {
            if(aggregateListener == null) {
                aggregateListener = listener;
            } else {
                throw new IllegalArgumentException("There is already an aggregate listener in " + listener.aggregateRootClass().orElseThrow() + " for message " + listener.consumedMessageClass().getName());
            }
        } else if(priority == MessageListenerType.REPOSITORY) {
            if(repositoryListener == null) {
                repositoryListener = listener;
            } else {
                throw new IllegalArgumentException("There is already a repository listener in " + listener.aggregateRootClass().orElseThrow() + " for message " + listener.consumedMessageClass().getName());
            }
        } else {
            throw new UnsupportedOperationException("Unsupported priority " + priority);
        }
    }

    private MessageListener factoryListener;

    private MessageListener aggregateListener;

    private MessageListener repositoryListener;
}
