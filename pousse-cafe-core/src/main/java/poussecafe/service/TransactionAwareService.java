package poussecafe.service;

import java.util.function.Supplier;
import poussecafe.process.ProcessManagerKey;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class TransactionAwareService {

    private TransactionRunner runner;

    public void setTransactionRunner(TransactionRunner runner) {
        checkThat(value(runner).notNull().because("Transaction runner cannot be null"));
        this.runner = runner;
    }

    protected void runInTransaction(Runnable runnable) {
        runner.runInTransaction(runnable);
    }

    protected ProcessManagerKey runInTransaction(Supplier<ProcessManagerKey> supplier) {
        return runner.runInTransaction(supplier);
    }
}
