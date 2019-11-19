package poussecafe.storage.internal;

import poussecafe.storage.DirectMessageSending;
import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.Storage;
import poussecafe.storage.TransactionRunner;

public class InternalStorage extends Storage {

    public static final String NAME = "internal";

    public static InternalStorage instance() {
        return SINGLETON;
    }

    private static final InternalStorage SINGLETON = new InternalStorage();

    private InternalStorage() {

    }

    @Override
    protected MessageSendingPolicy initMessageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    protected TransactionRunner initTransactionRunner() {
        return new NoTransactionRunner();
    }

    @Override
    public String name() {
        return NAME;
    }
}
