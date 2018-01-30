package poussecafe.storage;

public class InMemoryStorage extends ServiceCachingStorage {

    @Override
    protected MessageSendingPolicy newMessageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    protected TransactionRunner newTransactionRunner() {
        return new NoTransactionRunner();
    }

}
