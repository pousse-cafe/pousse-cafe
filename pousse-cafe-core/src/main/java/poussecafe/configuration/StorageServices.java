package poussecafe.configuration;

import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;
import poussecafe.storage.ConsequenceEmissionPolicy;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorageServices<K, D extends StorableData<K>> {

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    private TransactionRunner transactionRunner;

    private StorableDataAccess<K, D> dataAccess;

    private StorableDataFactory<D> dataFactory;

    public StorageServices(ConsequenceEmissionPolicy consequenceEmissionPolicy, TransactionRunner transactionRunner,
            StorableDataAccess<K, D> dataAccess, StorableDataFactory<D> dataFactory) {
        setConsequenceEmissionPolicy(consequenceEmissionPolicy);
        setTransactionRunner(transactionRunner);
        setDataAccess(dataAccess);
        setDataFactory(dataFactory);
    }

    public ConsequenceEmissionPolicy getConsequenceEmissionPolicy() {
        return consequenceEmissionPolicy;
    }

    private void setConsequenceEmissionPolicy(ConsequenceEmissionPolicy consequenceEmissionPolicy) {
        checkThat(value(consequenceEmissionPolicy).notNull());
        this.consequenceEmissionPolicy = consequenceEmissionPolicy;
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
