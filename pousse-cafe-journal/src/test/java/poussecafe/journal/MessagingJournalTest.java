package poussecafe.journal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.context.TransactionRunnerLocator;
import poussecafe.domain.ComponentFactory;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.journal.process.MessagingJournal;
import poussecafe.messaging.FailedConsumption;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.SuccessfulConsumption;
import poussecafe.messaging.TransparentMessageAdapter;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.FieldAccessor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class MessagingJournalTest {

    @Mock
    protected JournalEntryRepository entryRepository;

    @Mock
    protected JournalEntryFactory entryFactory;

    @Mock
    protected ComponentFactory primitiveFactory;

    @InjectMocks
    protected MessagingJournal journal;

    protected MessageAdapter messageAdapter;

    protected TransactionRunner transactionRunner;

    protected String listenerId;

    protected Message message;

    protected String exception;

    protected JournalEntryKey key;

    protected void givenConfiguredMessagingJournal() {
        MockitoAnnotations.initMocks(this);
        transactionRunner = transactionRunner();
        FieldAccessor journalAccessor = new FieldAccessor(journal);
        journalAccessor.set("transactionRunnerLocator", storageServiceLocator(transactionRunner));

        messageAdapter = new TransparentMessageAdapter();
    }

    protected TransactionRunner transactionRunner() {
        return mock(TransactionRunner.class);
    }

    private TransactionRunnerLocator storageServiceLocator(TransactionRunner transactionRunner) {
        TransactionRunnerLocator locator = mock(TransactionRunnerLocator.class);
        when(locator.locateTransactionRunner(JournalEntry.class)).thenReturn(transactionRunner);
        return locator;
    }

    protected void givenSuccessfullyConsumedMessage() {
        givenReceivedMessage();
    }

    protected void givenReceivedMessage() {
        listenerId = "listenerId";
        message = new TestMessage();
        givenKey();
    }

    protected void givenKey() {
        key = new JournalEntryKey("consumptionId", listenerId);
    }

    protected void givenIgnoredMessage() {
        givenReceivedMessage();
    }

    protected void givenMessageConsumptionFailed() {
        givenReceivedMessage();
        exception = "error";
    }

    protected void whenLoggingSuccessfulConsumption() {
        journal.logSuccessfulConsumption(new SuccessfulConsumption(key.getConsumptionId(), listenerId, message));
    }

    protected void whenLoggingFailedConsumption() {
        journal.logFailedConsumption(new FailedConsumption(key.getConsumptionId(), listenerId, message, exception));
    }

}
