package poussecafe.storage;

import java.util.function.Supplier;
import poussecafe.process.ProcessManagerKey;

public interface TransactionRunner {

    void runInTransaction(Runnable runnable);

    ProcessManagerKey runInTransaction(Supplier<ProcessManagerKey> supplier);
}
