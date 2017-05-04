package poussecafe.configuration;

import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;
import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorageServices<K, D extends StorableData<K>> {

    private MessageSendingPolicy messageSendingPolicy;

    private TransactionRunner transactionRunner;

    private StorableDataAccess<K, D> dataAccess;

    private StorableDataFactory<D> dataFactory;

    public StorageServices(MessageSendingPolicy messageSendingPolicy, TransactionRunner transactionRunner,
            StorableDataAccess<K, D> dataAccess, StorableDataFactory<D> dataFactory) {
        setMessageSendingPolicy(messageSendingPolicy);
        setTransactionRunner(transactionRunner);
        setDataAccess(dataAccess);
        setDataFactory(dataFactory);
    }

    public MessageSendingPolicy getMessageSendingPolicy() {
        return messageSendingPolicy;
    }

    private void setMessageSendingPolicy(MessageSendingPolicy messageSendingPolicy) {
        checkThat(value(messageSendingPolicy).notNull());
        this.messageSendingPolicy = messageSendingPolicy;
    }

    public TransactionRunner getTransactionRunner() {
        return transactionRunner;
    }

    private void setTransactionRunner(TransactionRunner transactionRunner) {
        checkThat(value(transactionRunner).notNull());
        this.transactionRunner = transactionRunner;
    }

    public StorableDataAccess<K, D> getDataAccess() {
        return dataAccess;
    }

    private void setDataAccess(StorableDataAccess<K, D> dataAccess) {
        checkThat(value(dataAccess).notNull());
        this.dataAccess = dataAccess;
    }

    public StorableDataFactory<D> getDataFactory() {
        return dataFactory;
    }

    private void setDataFactory(StorableDataFactory<D> dataFactory) {
        checkThat(value(dataFactory).notNull());
        this.dataFactory = dataFactory;
    }
}
