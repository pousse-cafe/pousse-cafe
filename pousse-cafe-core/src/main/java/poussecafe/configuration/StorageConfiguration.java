package poussecafe.configuration;

import poussecafe.storage.ConsequenceEmissionPolicy;
import poussecafe.storage.TransactionRunner;

public abstract class StorageConfiguration {

    private Singleton<ConsequenceEmissionPolicy> consequenceEmissionPolicy;

    private Singleton<TransactionRunner> transactionRunner;

    public StorageConfiguration() {
        consequenceEmissionPolicy = new Singleton<>(this::consequenceEmissionPolicy);
        transactionRunner = new Singleton<>(this::transactionRunner);
    }

    protected abstract ConsequenceEmissionPolicy consequenceEmissionPolicy();

    protected abstract TransactionRunner transactionRunner();

    public Singleton<ConsequenceEmissionPolicy> getConsequenceEmissionPolicy() {
        return consequenceEmissionPolicy;
    }

    public Singleton<TransactionRunner> getTransactionRunner() {
        return transactionRunner;
    }
}
