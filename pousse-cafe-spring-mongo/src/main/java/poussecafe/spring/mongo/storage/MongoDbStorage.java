package poussecafe.spring.mongo.storage;

import poussecafe.storage.DirectMessageSending;
import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.storage.Storage;
import poussecafe.storage.TransactionRunner;

public class MongoDbStorage extends Storage {

    public static final String NAME = "mongo";

    public static MongoDbStorage instance() {
        return INSTANCE;
    }

    private static final MongoDbStorage INSTANCE = new MongoDbStorage();

    @Override
    protected MessageSendingPolicy initMessageSendingPolicy() {
        return new DirectMessageSending();
    }

    @Override
    protected TransactionRunner initTransactionRunner() {
        return new NoTransactionRunner();
    }

    private MongoDbStorage() {

    }

    @Override
    protected String name() {
        return NAME;
    }
}
