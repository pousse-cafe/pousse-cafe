package poussecafe.storage;

import poussecafe.configuration.StorageConfiguration;

public class TransactionLessStorage extends StorageConfiguration {

    @Override
    public ConsequenceEmissionPolicy consequenceEmissionPolicy() {
        return new DirectEmissionPolicy();
    }

    @Override
    public TransactionRunner transactionRunner() {
        return new NoTransactionRunner();
    }

}
