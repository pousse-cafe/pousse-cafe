package poussecafe.processing;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.processing.MessageToProcess.Callback;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessageProcessingThreadTest implements Callback {

    @Test
    public void startedThreadEventuallyProcessesMessages() {
        givenProcessorNotFailing();
        givenStartedThread();
        givenMessagesToProcess();
        whenSubmittingMessagesAndStopping();
        thenMessagesHandled();
    }

    private void givenProcessorNotFailing() {
        messageProcessor = mock(MessageProcessor.class);
    }

    private void givenStartedThread() {
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
            MessageListenersGroup receivedMessage = mock(MessageListenersGroup.class);
            MessageToProcess message = new MessageToProcess.Builder()
                    .receivedMessageId(index)
                    .messageListenerGroup(receivedMessage)
                    .callback(this)
                    .build();
            messages.add(message);
            messageProcessed.add(false);
        }
    }

    private List<MessageToProcess> messages;

    private void whenSubmittingMessagesAndStopping() {
        whenSubmittingMessages();
        thread.stopAndWait(Duration.ofSeconds(3));
    }

    private void whenSubmittingMessages() {
        for(MessageToProcess messageToProcess : messages) {
            thread.submit(messageToProcess);
        }
    }

    private void thenMessagesHandled() {
        for(int index = 0; index < messages.size(); ++index) {
            verify(messageProcessor).processMessage(messages.get(index).messageListenerGroup());
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

    @Test
    public void anyExceptionThrownByProcessorExceptFailFastIsCaughtAndMessageAcked() {
        givenFailinProcessor();
        givenStartedThread();
        givenMessagesToProcess();
        whenSubmittingMessagesAndStopping();
        thenMessagesHandled();
    }

    private void givenFailinProcessor() {
        messageProcessor = mock(MessageProcessor.class);
        doThrow(RuntimeException.class).when(messageProcessor).processMessage(any(MessageListenersGroup.class));
    }
}
