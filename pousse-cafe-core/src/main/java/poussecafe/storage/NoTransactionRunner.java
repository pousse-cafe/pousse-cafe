package poussecafe.storage;

public class NoTransactionRunner implements TransactionRunner {

    @Override
    public void runInTransaction(Runnable runnable) {
        runnable.run();
    }

}
