package poussecafe.journal;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class LogsRunInTransactionTest extends MessagingJournalTest {

    @Test
    public void logSuccessfulConsumptionRunsInTransaction() {
        givenConfiguredMessagingJournal();
        givenSuccessfullyConsumedMessage();
        whenLoggingSuccessfulConsumption();
        thenTransactionRunnerIsInvoked();
    }

    private void thenTransactionRunnerIsInvoked() {
        verify(transactionRunner).runInTransaction(any(Runnable.class));
    }

    @Test
    public void logFailedConsumptionRunsInTransaction() {
        givenConfiguredMessagingJournal();
        givenMessageConsumptionFailed();
        whenLoggingFailedConsumption();
        thenTransactionRunnerIsInvoked();
    }
}
