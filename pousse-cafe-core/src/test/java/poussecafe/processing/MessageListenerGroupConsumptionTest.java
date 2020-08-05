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
        factoryListener = Optional.of(listener("factoryListener"));
        repositoryListener = Optional.of(listener("repositoryListener"));
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

    private Optional<MessageListener> factoryListener = Optional.empty();

    private Optional<MessageListener> repositoryListener = Optional.empty();

    private List<MessageListener> otherListeners = new ArrayList<>();

    private void givenUpdateListenersWithFirstHavingFailingRunner() {
        aggregateListener = Optional.of(failingRunnerListener());
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

    private Optional<MessageListener> aggregateListener = Optional.empty();

    private void whenExecuting() {
        MessageListenerGroupConsumption consumption = new MessageListenerGroupConsumption.Builder()
                .aggregateListener(aggregateListener)
                .aggregateRootClass(Optional.of(SimpleAggregate.class))
                .messageConsumptionContext(new MessageConsumptionContext.Builder()
                        .applicationPerformanceMonitoring(applicationPerformanceMonitoring())
                        .messageConsumptionHandler(messageConsumptionHandler())
                        .messageConsumptionConfiguration(MessageConsumptionConfiguration.defaultConfiguration())
                        .build())
                .consumptionState(groupConsumptionState)
                .factoryListener(factoryListener)
                .otherListeners(otherListeners)
                .repositoryListener(repositoryListener)
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
        factoryListener.ifPresent(this::listenerExecuted);
        repositoryListener.ifPresent(this::listenerExecuted);
        allListenersExecuted(otherListeners);
    }

    private void allListenersExecuted(List<MessageListener> listeners) {
        for(MessageListener listener : listeners) {
            listenerExecuted(listener);
        }
    }

    private void listenerExecuted(MessageListener listener) {
        verify(listener.consumer()).consume(groupConsumptionState);
    }
}
