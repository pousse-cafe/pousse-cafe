package poussecafe.journal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.context.TransactionRunnerLocator;
import poussecafe.domain.ComponentFactory;
import poussecafe.journal.data.SerializedMessage;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.journal.process.MessagingJournal;
import poussecafe.messaging.FailedConsumption;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.SuccessfulConsumption;
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

    protected SerializedMessage serializedMessage;

    protected String exception;

    protected JournalEntryKey key;

    protected void givenConfiguredMessagingJournal() {
        MockitoAnnotations.initMocks(this);
        transactionRunner = transactionRunner();
        FieldAccessor journalAccessor = new FieldAccessor(journal);
        journalAccessor.set("transactionRunnerLocator", storageServiceLocator(transactionRunner));

        messageAdapter = new MessageAdapter() {
            @Override
            public SerializedMessage adaptMessage(Message message) {
                return serializedMessage;
            }

            @Override
            public Message adaptSerializedMessage(SerializedMessage serializedMessage) {
                return message;
            }
        };
        journalAccessor.set("messageAdapter", messageAdapter);
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
        message = mock(Message.class);

        String messageId = "messageId";
        when(message.getId()).thenReturn(messageId);
        when(message.getType()).thenReturn(Message.class.getName());

        serializedMessage = mock(SerializedMessage.class);
        when(serializedMessage.getId()).thenReturn(messageId);

        givenKey();
    }

    protected void givenKey() {
        key = new JournalEntryKey(message.getId(), listenerId);
    }

    protected void givenIgnoredMessage() {
        givenReceivedMessage();
    }

    protected void givenMessageConsumptionFailed() {
        givenReceivedMessage();
        exception = "error";
    }

    protected void whenLoggingSuccessfulConsumption() {
        journal.logSuccessfulConsumption(new SuccessfulConsumption(listenerId, message));
    }

    protected void whenLoggingFailedConsumption() {
        journal.logFailedConsumption(new FailedConsumption(listenerId, message, exception));
    }

}
