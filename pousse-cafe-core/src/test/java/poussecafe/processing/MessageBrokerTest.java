package poussecafe.processing;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageBrokerTest {

    @Test
    public void receivedMessageDispatchedToAllThreads() {
        givenThreadPool();
        givenBroker();
        givenReceivedMessage();
        whenSubmittingReceivedMessage();
        thenMessageForwardedToAllThreads();
    }

    private void givenThreadPool() {
        threadPool = mock(MessageProcessingThreadPool.class);
        when(threadPool.size()).thenReturn(THREAD_POOL_SIZE);
    }

    private MessageProcessingThreadPool threadPool;

    private static final int THREAD_POOL_SIZE = 42;

    private void givenBroker() {
        broker = new MessageBroker(threadPool);
    }

    private MessageBroker broker;

    private void givenReceivedMessage() {
        receivedMessage = mock(ReceivedMessage.class);

        payload = mock(OriginalAndMarshaledMessage.class);
        when(receivedMessage.message()).thenReturn(payload);
    }

    private ReceivedMessage receivedMessage;

    private OriginalAndMarshaledMessage payload;

    private void whenSubmittingReceivedMessage() {
        broker.dispatch(receivedMessage);
    }

    private void thenMessageForwardedToAllThreads() {
        ArgumentCaptor<MessageToProcess> captor = ArgumentCaptor.forClass(MessageToProcess.class);
        verify(threadPool).submit(captor.capture());
        MessageToProcess messageToProcess = captor.getValue();
        assertThat(messageToProcess.receivedMessagePayload(), is(payload));
    }

    @Test
    public void receivedMessageAckedWhenAllProcessed() {
        givenAutoAckingThreadPool();
        givenBroker();
        givenReceivedMessage();
        whenSubmittingReceivedMessage();
        thenReceivedMessageAcked();
    }

    private void givenAutoAckingThreadPool() {
        givenThreadPool();
        doAnswer(autoAck()).when(threadPool).submit(any(MessageToProcess.class));
    }

    private Answer<Void> autoAck() {
        return invocation -> {
            for(int index = 0; index < THREAD_POOL_SIZE; ++index) {
                MessageToProcess messageToProcess = invocation.getArgument(0);
                messageToProcess.signalProcessed(index);
            }
            return null;
        };
    }

    private void thenReceivedMessageAcked() {
        verify(receivedMessage).ack();
    }

    @Test(expected = FailFastException.class)
    public void failFastThrowsOnSubsequentDispath() {
        givenAutoFailingThreadPool();
        givenBroker();
        givenReceivedMessage();
        givenFirstMessageCausesFailure();
        whenSubmittingReceivedMessage();
    }

    private void givenAutoFailingThreadPool() {
        givenThreadPool();
        doAnswer(autoFail()).when(threadPool).submit(any(MessageToProcess.class));
    }

    private Answer<Void> autoFail() {
        return invocation -> {
            MessageToProcess messageToProcess = invocation.getArgument(0);
            messageToProcess.failFast();
            return null;
        };
    }

    private void givenFirstMessageCausesFailure() {
        whenSubmittingReceivedMessage();
    }
}
