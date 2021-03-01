package poussecafe.environment;

import org.mockito.Mockito;
import poussecafe.domain.AggregateRoot;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.NoTransactionRunner;

import static org.mockito.Mockito.when;

public class TransactionRunnerLocatorMock {

    @SuppressWarnings("rawtypes")
    public static TransactionRunnerLocator mock(Class<? extends AggregateRoot> rootClass) {
        TransactionRunnerLocator transactionRunnerLocator = Mockito.mock(TransactionRunnerLocator.class);
        when(transactionRunnerLocator.locateTransactionRunner(rootClass)).thenReturn(new NoTransactionRunner());
        return transactionRunnerLocator;
    }

    private TransactionRunnerLocatorMock() {

    }
}
