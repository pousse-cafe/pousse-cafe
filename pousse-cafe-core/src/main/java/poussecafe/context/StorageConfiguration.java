package poussecafe.context;

import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.TransactionRunner;

public abstract class StorageConfiguration {

    private Singleton<MessageSendingPolicy> messageSendingPolicy;

    private Singleton<TransactionRunner> transactionRunner;

    public StorageConfiguration() {
        messageSendingPolicy = new Singleton<>(this::messageSendingPolicy);
        transactionRunner = new Singleton<>(this::transactionRunner);
    }

    protected abstract MessageSendingPolicy messageSendingPolicy();

    protected abstract TransactionRunner transactionRunner();

    public Singleton<MessageSendingPolicy> getMessageSendingPolicy() {
        return messageSendingPolicy;
    }

    public Singleton<TransactionRunner> getTransactionRunner() {
        return transactionRunner;
    }
}
