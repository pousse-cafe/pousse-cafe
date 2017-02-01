package poussecafe.journal;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class LogsRunInTransactionTest extends ConsequenceJournalTest {

    @Test
    public void logSuccessfulConsumptionRunsInTransaction() {
        givenConfiguredConsequenceJournal();
        givenSuccessfullyConsumedConsequence();
        whenLoggingSuccessfulConsumption();
        thenTransactionRunnerIsInvoked();
    }

    private void thenTransactionRunnerIsInvoked() {
        verify(transactionRunner).runInTransaction(any(Runnable.class));
    }

    @Test
    public void logIgnoredConsumptionRunsInTransaction() {
        givenConfiguredConsequenceJournal();
        givenIgnoredConsequence();
        whenLoggingIgnoredConsumption();
        thenTransactionRunnerIsInvoked();
    }

    @Test
    public void logFailedConsumptionRunsInTransaction() {
        givenConfiguredConsequenceJournal();
        givenConsequenceConsumptionFailed();
        whenLoggingFailedConsumption();
        thenTransactionRunnerIsInvoked();
    }
}
