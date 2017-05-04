package poussecafe.journal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.configuration.StorageServiceLocator;
import poussecafe.messaging.Message;
import poussecafe.storage.TransactionRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class MessagingJournalTest {

    @Mock
    protected JournalEntryRepository entryRepository;

    @Mock
    protected JournalEntryFactory entryFactory;

    @InjectMocks
    protected MessagingJournal journal;

    protected TransactionRunner transactionRunner;

    protected String listenerId;

    protected Message message;

    protected Exception exception;

    protected void givenConfiguredMessagingJournal() {
        MockitoAnnotations.initMocks(this);
        transactionRunner = transactionRunner();
        journal.setStorageServiceLocator(storageServiceLocator(transactionRunner));
    }

    protected TransactionRunner transactionRunner() {
        return mock(TransactionRunner.class);
    }

    private StorageServiceLocator storageServiceLocator(TransactionRunner transactionRunner) {
        StorageServiceLocator locator = mock(StorageServiceLocator.class);
        when(locator.locateTransactionRunner(JournalEntry.Data.class)).thenReturn(transactionRunner);
        return locator;
    }

    protected void givenSuccessfullyConsumedMessage() {
        givenReceivedMessage();
    }

    protected void givenReceivedMessage() {
        listenerId = "listenerId";
        message = mock(Message.class);
        when(message.getId()).thenReturn("messageId");
    }

    protected void givenIgnoredMessage() {
        givenReceivedMessage();
    }

    protected void givenMessageConsumptionFailed() {
        givenReceivedMessage();
        exception = new Exception();
    }

    protected void whenLoggingSuccessfulConsumption() {
        journal.logSuccessfulConsumption(listenerId, new SuccessfulConsumption(message));
    }

    protected void whenLoggingIgnoredConsumption() {
        journal.logIgnoredConsumption(listenerId, message);
    }

    protected void whenLoggingFailedConsumption() {
        journal.logFailedConsumption(listenerId, message, exception);
    }

}
