package poussecafe.journal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.context.StorageServiceLocator;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.storable.PrimitiveFactory;
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
    protected PrimitiveFactory primitiveFactory;

    @InjectMocks
    protected MessagingJournal journal;

    protected MessageAdapter messageAdapter;


    protected TransactionRunner transactionRunner;

    protected String listenerId;

    protected Message message;

    protected SerializedMessage serializedMessage;

    protected Exception exception;

    protected JournalEntryKey key;

    protected void givenConfiguredMessagingJournal() {
        MockitoAnnotations.initMocks(this);
        transactionRunner = transactionRunner();
        journal.setStorageServiceLocator(storageServiceLocator(transactionRunner));

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
        new FieldAccessor(journal).set("messageAdapter", messageAdapter);
    }

    protected TransactionRunner transactionRunner() {
        return mock(TransactionRunner.class);
    }

    private StorageServiceLocator storageServiceLocator(TransactionRunner transactionRunner) {
        StorageServiceLocator locator = mock(StorageServiceLocator.class);
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
