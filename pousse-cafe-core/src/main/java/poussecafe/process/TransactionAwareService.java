package poussecafe.process;

import poussecafe.context.TransactionRunnerLocator;

public abstract class TransactionAwareService {

    private TransactionRunnerLocator transactionRunnerLocator;

    protected void runInTransaction(Class<?> storableClass,
            Runnable runnable) {
        transactionRunnerLocator.locateTransactionRunner(storableClass).runInTransaction(runnable);
    }
}
