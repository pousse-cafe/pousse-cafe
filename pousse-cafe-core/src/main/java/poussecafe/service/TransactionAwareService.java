package poussecafe.service;

import poussecafe.configuration.StorageServiceLocator;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class TransactionAwareService {

    private StorageServiceLocator storageServiceLocator;

    public void setStorageServiceLocator(StorageServiceLocator transactionRunnerLocator) {
        checkThat(value(transactionRunnerLocator).notNull().because("Storage service locator cannot be null"));
        storageServiceLocator = transactionRunnerLocator;
    }

    protected void runInTransaction(Class<?> storableClass,
            Runnable runnable) {
        storageServiceLocator.locateTransactionRunner(storableClass).runInTransaction(runnable);
    }
}
