package poussecafe.journal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.consequence.Consequence;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ConsequenceJournalTest {

    @Mock
    protected EntryRepository entryRepository;

    @Mock
    protected EntryFactory entryFactory;

    @InjectMocks
    protected ConsequenceJournal journal;

    protected TransactionRunner transactionRunner;

    protected String listenerId;

    protected Consequence consequence;

    protected Exception exception;

    protected void givenConfiguredConsequenceJournal() {
        MockitoAnnotations.initMocks(this);
        transactionRunner = transactionRunner();
        journal.setTransactionRunner(transactionRunner);
    }

    protected TransactionRunner transactionRunner() {
        return mock(TransactionRunner.class);
    }

    protected void givenSuccessfullyConsumedConsequence() {
        givenReceivedConsequence();
    }

    protected void givenReceivedConsequence() {
        listenerId = "listenerId";
        consequence = mock(Consequence.class);
        when(consequence.getId()).thenReturn("consequenceId");
    }

    protected void givenIgnoredConsequence() {
        givenReceivedConsequence();
    }

    protected void givenConsequenceConsumptionFailed() {
        givenReceivedConsequence();
        exception = new Exception();
    }

    protected void whenLoggingSuccessfulConsumption() {
        journal.logSuccessfulConsumption(listenerId, consequence);
    }

    protected void whenLoggingIgnoredConsumption() {
        journal.logIgnoredConsumption(listenerId, consequence);
    }

    protected void whenLoggingFailedConsumption() {
        journal.logFailedConsumption(listenerId, consequence, exception);
    }

}
