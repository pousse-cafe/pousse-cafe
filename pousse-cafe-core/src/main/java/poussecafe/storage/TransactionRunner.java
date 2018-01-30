package poussecafe.storage;

public interface TransactionRunner {

    void runInTransaction(Runnable runnable);
}
