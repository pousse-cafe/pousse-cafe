package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.domain.DomainEvent;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.MessageConsumer;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.SampleEventData;
import poussecafe.messaging.Message;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.runtime.TestDomainEvent;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageListenerExecutorTest {

    @Test
    public void withExpectedEventsFalseSkipsCheck() {
        givenConsumptionStateAndConsumptionHandler();
        givenListenerWithExpectedEventsFalse();
        whenExecuting();
        thenProducedEventsNotChecked();
        thenExecutionSuccess();
    }

    private void givenListenerWithExpectedEventsFalse() {
        listener = mock(MessageListener.class);
        when(listener.withExpectedEvents()).thenReturn(false);
        MessageConsumer consumer = mock(MessageConsumer.class);
        MessageListenerConsumptionReport report = mock(MessageListenerConsumptionReport.class);
        when(report.isSuccess()).thenReturn(true);
        when(consumer.consume(consumptionState)).thenReturn(report);
        when(listener.consumer()).thenReturn(consumer);
    }

    private MessageListener listener;

    private void givenConsumptionStateAndConsumptionHandler() {
        consumptionState = mock(MessageListenerGroupConsumptionState.class);
        when(consumptionState.isFirstConsumption()).thenReturn(true);
        OriginalAndMarshaledMessage receivedMessage = mock(OriginalAndMarshaledMessage.class);
        Message original = mock(Message.class);
        when(receivedMessage.original()).thenReturn(original);
        when(consumptionState.message()).thenReturn(receivedMessage);

        messageConsumptionHandler = mock(MessageConsumptionHandler.class);
    }

    private MessageListenerGroupConsumptionState consumptionState;

    private MessageConsumptionHandler messageConsumptionHandler;

    private void whenExecuting() {
        executor = new MessageListenerExecutor.Builder()
                .listener(listener)
                .consumptionState(consumptionState)
                .messageConsumptionHandler(messageConsumptionHandler)
                .build();
        executor.executeListener();
    }

    private void thenProducedEventsNotChecked() {
        verify(listener, never()).expectedEvents();
    }

    private MessageListenerExecutor executor;

    private void thenExecutionSuccess() {
        assertTrue(executor.messageConsumptionReport().isSuccess());
    }

    @Test
    public void eventsChecked() {
        givenExpectedEvent(expectedEventMatching(SampleEventData.class));
        givenProducedEvent(new SampleEventData());
        givenConsumptionStateAndConsumptionHandler();
        givenListenerWithExpectedEvents();
        whenExecuting();
        thenProducedEventsChecked();
    }

    private ExpectedEvent expectedEventMatching(Class<? extends DomainEvent> producedEvent) {
        ExpectedEvent expectedEvent = mock(ExpectedEvent.class);
        when(expectedEvent.matches(any(producedEvent))).thenReturn(true);
        return expectedEvent;
    }

    private void givenExpectedEvent(ExpectedEvent expectedEvent) {
        expectedEvents.add(expectedEvent);
    }

    private List<ExpectedEvent> expectedEvents = new ArrayList<>();

    private <E extends DomainEvent> void givenProducedEvent(E producedEvent) {
        producedEvents.add(producedEvent);
    }

    private List<DomainEvent> producedEvents = new ArrayList<>();

    private void givenListenerWithExpectedEvents() {
        listener = mock(MessageListener.class);
        when(listener.shortId()).thenReturn("listenerId");
        when(listener.withExpectedEvents()).thenReturn(true);
        when(listener.expectedEvents()).thenReturn(expectedEvents);
        MessageConsumer consumer = mock(MessageConsumer.class);
        MessageListenerConsumptionReport report = mock(MessageListenerConsumptionReport.class);
        when(report.producedEvents()).thenReturn(producedEvents);
        when(report.isSuccess()).thenReturn(true);
        when(consumer.consume(consumptionState)).thenReturn(report);
        when(listener.consumer()).thenReturn(consumer);
    }

    private void thenProducedEventsChecked() {
        for(ExpectedEvent expectedEvent : expectedEvents) {
            verify(expectedEvent).matches(any());
        }
        assertTrue(executor.messageConsumptionReport().isSuccess());
    }

    @Test
    public void expectedRequiredEventMissingFails() {
        givenExpectedEvent(requiredExpectedEventMatching(SampleEventData.class));
        givenConsumptionStateAndConsumptionHandler();
        givenListenerWithExpectedEvents();
        whenExecuting();
        thenExecutionFailed();
    }

    private ExpectedEvent requiredExpectedEventMatching(Class<SampleEventData> producedEvent) {
        ExpectedEvent expectedEvent = mock(ExpectedEvent.class);
        when(expectedEvent.matches(any(producedEvent))).thenReturn(true);
        when(expectedEvent.required()).thenReturn(true);
        return expectedEvent;
    }

    private void thenExecutionFailed() {
        assertTrue(executor.messageConsumptionReport().isFailed());
    }

    @Test
    public void unexpectedEventFails() {
        givenExpectedEvent(expectedEventMatching(SampleEventData.class));
        givenProducedEvent(new TestDomainEvent());
        givenConsumptionStateAndConsumptionHandler();
        givenListenerWithExpectedEvents();
        whenExecuting();
        thenExecutionFailed();
    }
}
