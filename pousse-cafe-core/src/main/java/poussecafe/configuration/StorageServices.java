package poussecafe.configuration;

import poussecafe.storable.IdentifiedStorableDataAccess;
import poussecafe.storable.StorableData;
import poussecafe.storage.MessageSendingPolicy;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorageServices<D extends StorableData> {

    private MessageSendingPolicy messageSendingPolicy;

    private TransactionRunner transactionRunner;

    private IdentifiedStorableDataAccess<D> dataAccess;

    public StorageServices(MessageSendingPolicy messageSendingPolicy, TransactionRunner transactionRunner,
            IdentifiedStorableDataAccess<D> dataAccess) {
        setMessageSendingPolicy(messageSendingPolicy);
        setTransactionRunner(transactionRunner);
        setDataAccess(dataAccess);
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

    public IdentifiedStorableDataAccess<D> getDataAccess() {
        return dataAccess;
    }

    private void setDataAccess(IdentifiedStorableDataAccess<D> dataAccess) {
        checkThat(value(dataAccess).notNull());
        this.dataAccess = dataAccess;
    }
}
