package poussecafe.process;

import poussecafe.runtime.TransactionRunnerLocator;

public abstract class TransactionAwareService {

    private TransactionRunnerLocator transactionRunnerLocator;

    protected void runInTransaction(Class<?> entityClass,
            Runnable runnable) {
        transactionRunnerLocator.locateTransactionRunner(entityClass).runInTransaction(runnable);
    }
}
