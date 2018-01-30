package poussecafe.storage;

public interface Storage {

    MessageSendingPolicy getMessageSendingPolicy();

    TransactionRunner getTransactionRunner();
}
