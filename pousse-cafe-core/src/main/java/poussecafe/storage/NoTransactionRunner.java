package poussecafe.storage;

import java.util.function.Supplier;
import poussecafe.process.ProcessManagerKey;

public class NoTransactionRunner implements TransactionRunner {

    @Override
    public void runInTransaction(Runnable runnable) {
        runnable.run();
    }

    @Override
    public ProcessManagerKey runInTransaction(Supplier<ProcessManagerKey> supplier) {
        return supplier.get();
    }

}
