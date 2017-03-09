package poussecafe.service;

import java.util.function.Supplier;
import poussecafe.configuration.StorageServiceLocator;
import poussecafe.process.ProcessManagerKey;
import poussecafe.storable.StorableData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class TransactionAwareService {

    private StorageServiceLocator storageServiceLocator;

    public void setStorageServiceLocator(StorageServiceLocator transactionRunnerLocator) {
        checkThat(value(transactionRunnerLocator).notNull().because("Storage service locator cannot be null"));
        this.storageServiceLocator = transactionRunnerLocator;
    }

    protected <D extends StorableData<?>> void runInTransaction(Class<D> dataClass,
            Runnable runnable) {
        storageServiceLocator.locateTransactionRunner(dataClass).runInTransaction(runnable);
    }

    protected <D extends StorableData<?>> ProcessManagerKey runInTransaction(Class<D> dataClass,
            Supplier<ProcessManagerKey> supplier) {
        return storageServiceLocator.locateTransactionRunner(dataClass).runInTransaction(supplier);
    }
}
