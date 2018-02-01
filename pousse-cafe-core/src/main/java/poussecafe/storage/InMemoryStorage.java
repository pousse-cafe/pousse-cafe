package poussecafe.storage;

public class InMemoryStorage extends ServiceCachingStorage {

    public static InMemoryStorage instance() {
        return SINGLETON;
    }

    private static final InMemoryStorage SINGLETON = new InMemoryStorage();

    @Override
    protected MessageSendingPolicy newMessageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    protected TransactionRunner newTransactionRunner() {
        return new NoTransactionRunner();
    }

}
