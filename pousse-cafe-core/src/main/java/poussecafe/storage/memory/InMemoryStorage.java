package poussecafe.storage.memory;

import poussecafe.storage.DirectMessageSending;
import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.Storage;
import poussecafe.storage.TransactionRunner;

public class InMemoryStorage extends Storage {

    public static InMemoryStorage instance() {
        return SINGLETON;
    }

    private static final InMemoryStorage SINGLETON = new InMemoryStorage();

    @Override
    protected MessageSendingPolicy initMessageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    protected TransactionRunner initTransactionRunner() {
        return new NoTransactionRunner();
    }

}
