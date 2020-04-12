package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.AggregateUpdateMessageConsumer;
import poussecafe.environment.MessageConsumer;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.testmodule.SimpleAggregate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageListenerGroupConsumptionTest {

    @Test
    public void runnerFailureDoesNotPreventExecutionOfOtherListeners() {
        givenOtherListeners();
        givenUpdateListenersWithFirstHavingFailingRunner();
        whenExecuting();
        thenListenersExecutedExceptOneWithFailingRunner();
    }

    private void givenOtherListeners() {
        factoryListeners.add(listener("factoryListener"));
        repositoryListeners.add(listener("repositoryListener"));
        otherListeners.add(listener("otherListener"));
    }

    private MessageListener listener(String listenerId) {
        MessageListener listener = mock(MessageListener.class);
        MessageConsumer consumer = mock(MessageConsumer.class);
        when(listener.consumer()).thenReturn(consumer);
        when(consumer.consume(groupConsumptionState)).thenReturn(new MessageListenerConsumptionReport.Builder("listenerId").build());
        when(listener.shortId()).thenReturn(listenerId);
        return listener;
    }

    private List<MessageListener> factoryListeners = new ArrayList<>();

    private List<MessageListener> repositoryListeners = new ArrayList<>();

    private List<MessageListener> otherListeners = new ArrayList<>();

    private void givenUpdateListenersWithFirstHavingFailingRunner() {
        aggregateListeners.add(failingRunnerListener());
        aggregateListeners.add(aggregateListener("successfulAggregateListener"));
    }

    private MessageListener failingRunnerListener() {
        MessageListener listener = mock(MessageListener.class);
        AggregateUpdateMessageConsumer consumer = mock(AggregateUpdateMessageConsumer.class);
        @SuppressWarnings("rawtypes")
        AggregateMessageListenerRunner failingRunner = failingRunner();
        when(consumer.runner()).thenReturn(failingRunner);
        when(listener.consumer()).thenReturn(consumer);
        when(listener.shortId()).thenReturn("failingRunnerListener");
        return listener;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private AggregateMessageListenerRunner failingRunner() {
        AggregateMessageListenerRunner failingRunner = mock(AggregateMessageListenerRunner.class);
        when(failingRunner.targetAggregates(original)).thenThrow(new RuntimeException());
        return failingRunner;
    }

    private MessageListener aggregateListener(String listenerId) {
        MessageListener listener = mock(MessageListener.class);
        AggregateUpdateMessageConsumer consumer = mock(AggregateUpdateMessageConsumer.class);
        when(consumer.consume(groupConsumptionState)).thenReturn(new MessageListenerConsumptionReport.Builder("listenerId").build());
        @SuppressWarnings("rawtypes")
        AggregateMessageListenerRunner successfulRunner = successfulRunner();
        when(consumer.runner()).thenReturn(successfulRunner);
        when(listener.consumer()).thenReturn(consumer);
        when(listener.shortId()).thenReturn(listenerId);
        return listener;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private AggregateMessageListenerRunner successfulRunner() {
        AggregateMessageListenerRunner successfulRunner = mock(AggregateMessageListenerRunner.class);
        TargetAggregates targetAggregates = new TargetAggregates.Builder()
                .toUpdate(aggregateId)
                .build();
        when(successfulRunner.targetAggregates(original)).thenReturn(targetAggregates);
        return successfulRunner;
    }

    private String aggregateId = "aggregateId";

    private List<MessageListener> aggregateListeners = new ArrayList<>();

    private void whenExecuting() {
        MessageListenerGroupConsumption consumption = new MessageListenerGroupConsumption.Builder()
                .aggregateListeners(aggregateListeners)
                .aggregateRootClass(Optional.of(SimpleAggregate.class))
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring())
                .consumptionState(groupConsumptionState)
                .factoryListeners(factoryListeners)
                .messageConsumptionHandler(messageConsumptionHandler())
                .otherListeners(otherListeners)
                .repositoryListeners(repositoryListeners)
                .build();
        consumption.execute();
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring() {
        ApplicationPerformanceMonitoring apm = mock(ApplicationPerformanceMonitoring.class);
        ApmTransaction apmTransaction = mock(ApmTransaction.class);
        when(apm.startTransaction(anyString())).thenReturn(apmTransaction);
        return apm;
    }

    private MessageListenerGroupConsumptionState groupConsumptionState = groupConsumptionState();

    private MessageListenerGroupConsumptionState groupConsumptionState() {
        MessageListenerGroupConsumptionState groupConsumptionState = mock(MessageListenerGroupConsumptionState.class);
        OriginalAndMarshaledMessage originalAndMarshaledMessage = mock(OriginalAndMarshaledMessage.class);
        original = mock(Message.class);
        when(originalAndMarshaledMessage.original()).thenReturn(original);
        when(groupConsumptionState.message()).thenReturn(originalAndMarshaledMessage);
        when(groupConsumptionState.isFirstConsumption()).thenReturn(true);
        return groupConsumptionState;
    }

    private Message original;

    private MessageConsumptionHandler messageConsumptionHandler() {
        return mock(MessageConsumptionHandler.class);
    }

    private void thenListenersExecutedExceptOneWithFailingRunner() {
        allListenersExecuted(factoryListeners);
        allListenersExecuted(repositoryListeners);
        allListenersExecuted(otherListeners);
        allListenersExecutedExceptFirst(aggregateListeners, 1);
    }

    private void allListenersExecuted(List<MessageListener> listeners) {
        for(MessageListener listener : listeners) {
            verify(listener.consumer()).consume(groupConsumptionState);
        }
    }

    private void allListenersExecutedExceptFirst(List<MessageListener> listeners, int firstSkipped) {
        for(int i = firstSkipped; i < listeners.size(); ++i) {
            MessageListener listener = listeners.get(i);
            AggregateUpdateMessageConsumer consumer = (AggregateUpdateMessageConsumer) listener.consumer();
            verify(consumer).consume(groupConsumptionState, aggregateId);
        }
    }
}
