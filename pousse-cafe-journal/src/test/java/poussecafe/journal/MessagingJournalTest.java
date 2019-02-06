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
import poussecafe.journal.process.StoreConsumptionResult;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.TransparentMessageAdapter;
import poussecafe.storage.TransactionRunner;
import poussecafe.support.adapters.messaging.FailedConsumptionData;
import poussecafe.support.adapters.messaging.SuccessfulConsumptionData;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;
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
    protected StoreConsumptionResult journal;

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
        SuccessfulConsumption event = new SuccessfulConsumptionData();
        event.consumptionId().value(key.getConsumptionId());
        event.listenerId().value(listenerId);
        event.rawMessage().value(rawMessage());
        journal.storeSuccessfulConsumption(event);
    }

    protected String rawMessage() {
        return "rawMessage";
    }

    protected void whenLoggingFailedConsumption() {
        FailedConsumption event = new FailedConsumptionData();
        event.consumptionId().value(key.getConsumptionId());
        event.listenerId().value(listenerId);
        event.rawMessage().value(rawMessage());
        event.error().value("error");
        journal.storeFailedConsumption(event);
    }
}
