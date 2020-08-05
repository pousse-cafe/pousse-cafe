package poussecafe.processing;

import java.util.List;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.messaging.Message;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageBrokerTest {

    @Test
    public void receivedMessageDispatchedToOneProcessingThread() {
        givenReceivedMessage();
        givenRuntime();
        givenBroker();
        whenSubmittingReceivedMessage();
        thenMessageForwardedToOneProcessingThread();
    }

    private void givenReceivedMessage() {
        receivedMessage = mock(ReceivedMessage.class);

        payload = mock(OriginalAndMarshaledMessage.class);
        Message originalMessage = mock(Message.class);
        when(payload.original()).thenReturn(originalMessage);
        when(receivedMessage.message()).thenReturn(payload);
    }

    private ReceivedMessage receivedMessage;

    private OriginalAndMarshaledMessage payload;

    private void givenRuntime() {
        threadPool = mock(MessageProcessingThreadPool.class);
        when(threadPool.size()).thenReturn(THREAD_POOL_SIZE);

        applicationPerformanceMonitoring = mock(ApplicationPerformanceMonitoring.class);
        messageConsumptionHandler = mock(MessageConsumptionHandler.class);

        listenersSet = mock(ListenersSet.class);
        MessageListener listener = mock(MessageListener.class);
        List<MessageListener> listeners = asList(listener);
        when(listenersSet.messageListenersOf(any())).thenReturn(listeners);
    }

    private MessageProcessingThreadPool threadPool;

    private static final int THREAD_POOL_SIZE = 42;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private MessageConsumptionHandler messageConsumptionHandler;

    private ListenersSet listenersSet;

    private void givenBroker() {
        broker = new MessageBroker.Builder()
                .messageConsumptionContext(new MessageConsumptionContext.Builder()
                        .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                        .messageConsumptionHandler(messageConsumptionHandler)
                        .messageConsumptionConfiguration(MessageConsumptionConfiguration.defaultConfiguration())
                        .build())
                .messageProcessingThreadsPool(threadPool)
                .listenersSet(listenersSet)
                .build();
    }

    private MessageBroker broker;

    private void whenSubmittingReceivedMessage() {
        broker.dispatch(receivedMessage);
    }

    private void thenMessageForwardedToOneProcessingThread() {
        ArgumentCaptor<Integer> threadIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<MessageToProcess> captor = ArgumentCaptor.forClass(MessageToProcess.class);
        verify(threadPool).submit(threadIdCaptor.capture(), captor.capture());
        MessageToProcess messageToProcess = captor.getValue();
        assertThat(messageToProcess.messageListenerGroup().message(), is(payload));
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
        givenRuntime();
        doAnswer(autoAck()).when(threadPool).submit(any(Integer.class), any(MessageToProcess.class));
    }

    private Answer<Void> autoAck() {
        return invocation -> {
            int threadId = invocation.getArgument(0);
            MessageToProcess messageToProcess = invocation.getArgument(1);
            messageToProcess.signalProcessed(threadId);
            return null;
        };
    }

    private void thenReceivedMessageAcked() {
        verify(receivedMessage).ack();
    }
}
