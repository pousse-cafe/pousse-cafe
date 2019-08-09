package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.processing.MessageToProcess.Callback;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessageProcessingThreadTest implements Callback {

    @Test
    public void startedThreadEventuallyProcessesMessages() {
        givenStartedProcessingThread();
        givenMessagesToProcess();
        whenSubmittingMessagesAndStopping();
        thenMessagesHandled();
    }

    private void givenStartedProcessingThread() {
        messageProcessor = mock(MessageProcessor.class);
        thread = new MessageProcessingThread.Builder()
                .threadId(0)
                .messageProcessor(messageProcessor)
                .build();
        thread.start();
    }

    private MessageProcessor messageProcessor;

    private MessageProcessingThread thread;

    private void givenMessagesToProcess() {
        messages = new ArrayList<>();
        messageProcessed = new ArrayList<>();

        for(int index = 0; index < 42; ++index) {
            OriginalAndMarshaledMessage receivedMessage = mock(OriginalAndMarshaledMessage.class);
            MessageToProcess message = new MessageToProcess.Builder()
                    .receivedMessageId(index)
                    .receivedMessagePayload(receivedMessage)
                    .callback(this)
                    .build();
            messages.add(message);
            messageProcessed.add(false);
        }
    }

    private List<MessageToProcess> messages;

    private void whenSubmittingMessagesAndStopping() {
        for(MessageToProcess messageToProcess : messages) {
            thread.submit(messageToProcess);
        }
        thread.stopAndWait();
    }

    private void thenMessagesHandled() {
        for(int index = 0; index < messages.size(); ++index) {
            verify(messageProcessor).processMessage(messages.get(index).receivedMessagePayload());
            assertTrue(messageProcessed.get(index));
        }
    }

    private List<Boolean> messageProcessed;

    @Override
    public synchronized void signalProcessed(int threadId, MessageToProcess processedMessage) {
        int index = (int) processedMessage.receivedMessageId();
        if(index > 0 && !messageProcessed.get(index - 1)) {
            throw new IllegalArgumentException("Previous message is not yet processed");
        }
        messageProcessed.set(index, true);
    }
}
