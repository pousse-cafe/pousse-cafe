package poussecafe.storage;

import poussecafe.configuration.StorageConfiguration;

public class TransactionLessStorage extends StorageConfiguration {

    @Override
    public MessageSendingPolicy messageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    public TransactionRunner transactionRunner() {
        return new NoTransactionRunner();
    }

}
