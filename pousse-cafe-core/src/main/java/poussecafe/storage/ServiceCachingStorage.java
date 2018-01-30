package poussecafe.storage;

public abstract class ServiceCachingStorage implements Storage {

    private MessageSendingPolicy policy;

    private TransactionRunner transactionRunner;

    public ServiceCachingStorage() {
        policy = newMessageSendingPolicy();
        transactionRunner = newTransactionRunner();
    }

    protected abstract MessageSendingPolicy newMessageSendingPolicy();

    protected abstract TransactionRunner newTransactionRunner();

    @Override
    public MessageSendingPolicy getMessageSendingPolicy() {
        return policy;
    }

    @Override
    public TransactionRunner getTransactionRunner() {
        return transactionRunner;
    }

}
