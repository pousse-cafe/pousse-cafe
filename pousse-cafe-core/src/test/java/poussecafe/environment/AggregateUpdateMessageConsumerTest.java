package poussecafe.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.processing.MessageListenersGroup;
import poussecafe.runtime.NoOpMessageValidator;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.DefaultMessageCollection;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.testmodule.SimpleMessage;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregateUpdateMessageConsumerTest {

    @Test
    public void skipImpliesReportSkip() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("skip");
        givenMessage();
        whenConsume();
        thenReportSkip();
    }

    private void givenAggregate() {
        aggregateRoot = new Aggregate();
        aggregateRoot.messageCollection(new DefaultMessageCollection());
        aggregateRoot.messageValidator(NoOpMessageValidator.instance());
    }

    private Aggregate aggregateRoot;

    public static class Aggregate extends AggregateRoot<String, EntityAttributes<String>> {

        public void skip(Message message) {
            throw new SameOperationException();
        }

        public void retry(Message message) {
            throw new RetryOperationException();
        }

        public void retryOrSkipByRunner(Message message) {

        }

        public void listenerProducingExpectedEvents(Message message) {
            var event = new SampleEventData();
            issue(event);
            event = new SampleEventData();
            issue(event);
        }

        public void listenerProducingNoEvent(Message message) {

        }

        public void listenerProducingUnexpectedEvent(Message message) {
            var event = new SampleEventData2();
            issue(event);
        }
    }

    private void givenAggregateUpdateMessageConsumerForMethod(String methodName) {
        repository = mock(Repository.class);
        when(repository.getOptional("id")).thenReturn(Optional.of(aggregateRoot));
        Factory factory = mock(Factory.class);
        AggregateServices aggregateServices = new AggregateServices(Aggregate.class, repository, factory);

        applicationPerformanceMonitoring = mock(ApplicationPerformanceMonitoring.class);
        ApmSpan currentSpan = mock(ApmSpan.class);
        when(applicationPerformanceMonitoring.currentSpan()).thenReturn(currentSpan);
        newSpan = mock(ApmSpan.class);
        when(currentSpan.startSpan()).thenReturn(newSpan);

        runner = mock(AggregateMessageListenerRunner.class);
        when(runner.targetAggregates(any())).thenReturn(new TargetAggregates.Builder<>()
                .toUpdate("id")
                .build());

        TransactionRunnerLocator transactionRunnerLocator = mock(TransactionRunnerLocator.class);
        when(transactionRunnerLocator.locateTransactionRunner(Aggregate.class)).thenReturn(new NoTransactionRunner());

        try {
            consumer = new AggregateUpdateMessageConsumer.Builder()
                    .listenerId("listenerId")
                    .aggregateServices(aggregateServices)
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .method(aggregateRoot.getClass().getDeclaredMethod(methodName, Message.class))
                    .runner(runner)
                    .transactionRunnerLocator(transactionRunnerLocator)
                    .hasExpectedEvents(hasExpectedEvents)
                    .expectedEvents(expectedEvents)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        messageListenersGroup = mock(MessageListenersGroup.class);
        when(messageListenersGroup.aggregateRootClass()).thenReturn(Optional.of(aggregateRoot.getClass()));
    }

    private Repository repository;

    private MessageListenersGroup messageListenersGroup;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private ApmSpan newSpan;

    private AggregateMessageListenerRunner runner;

    private AggregateUpdateMessageConsumer consumer;

    private void givenMessage() {
        message = new SimpleMessage();
    }

    private SimpleMessage message;

    private void whenConsume() {
        MessageListenerGroupConsumptionState state = new MessageListenerGroupConsumptionState.Builder()
                .message(new OriginalAndMarshaledMessage.Builder()
                    .marshaled(message)
                    .original(message)
                    .build())
                .isFirstConsumption(true)
                .messageListenersGroup(messageListenersGroup)
                .toUpdate(emptySet())
                .processorLogger(LoggerFactory.getLogger(getClass()))
                .consumptionId("consumptionId")
                .build();
        report = consumer.consume(state, "id");
    }

    private MessageListenerConsumptionReport report;

    private void thenReportSkip() {
        assertTrue(report.isSkipped());
    }

    @Test
    public void skipImpliesNoUpdate() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("skip");
        givenMessage();
        whenConsume();
        thenNoUpdate();
    }

    private void thenNoUpdate() {
        verify(repository, never()).update(aggregateRoot);
    }

    @Test
    public void skipImpliesNewSpanEnd() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("skip");
        givenMessage();
        whenConsume();
        thenNewSpanEnded();
    }

    private void thenNewSpanEnded() {
        verify(newSpan).end();
    }

    @Test
    public void retryImpliesReportRetry() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("retry");
        givenMessage();
        whenConsume();
        thenReportRetry();
    }

    private void thenReportRetry() {
        assertTrue(report.mustRetry());
    }

    @Test
    public void retryByRunnerImpliesReportRetry() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("retryOrSkipByRunner");
        givenMessage();
        givenRunnerRetries();
        whenConsume();
        thenReportRetry();
    }

    private void givenRunnerRetries() {
        doThrow(RetryOperationException.class).when(runner).validChronologyOrElseThrow(message, "id", Optional.of(aggregateRoot));
    }

    @Test
    public void skipByRunnerImpliesReportSkip() {
        givenAggregate();
        givenAggregateUpdateMessageConsumerForMethod("retryOrSkipByRunner");
        givenMessage();
        givenRunnerSkips();
        whenConsume();
        thenReportSkip();
    }

    private void givenRunnerSkips() {
        doThrow(SameOperationException.class).when(runner).validChronologyOrElseThrow(message, "id", Optional.of(aggregateRoot));
    }

    @Test
    public void producedEventsCheckSkippedIfHasNone() {
        givenAggregate();
        givenNoExpectedEvents();
        givenAggregateUpdateMessageConsumerForMethod("listenerProducingNoEvent");
        givenMessage();
        whenConsume();
        thenReportSuccess();
    }

    private void givenNoExpectedEvents() {
        // Default case
    }

    private boolean hasExpectedEvents = false;

    private List<ExpectedEvent> expectedEvents = new ArrayList<>();

    private void thenReportSuccess() {
        assertTrue(report.isSuccess());
    }

    @Test
    public void expectedEventsProducedSucceeds() {
        givenAggregate();
        givenExpectedEvents();
        givenAggregateUpdateMessageConsumerForMethod("listenerProducingExpectedEvents");
        givenMessage();
        whenConsume();
        thenReportSuccess();
    }

    private void givenExpectedEvents() {
        hasExpectedEvents = true;
        var expectedEvent = new ExpectedEvent.Builder()
                .producedEventClass(SampleEvent.class)
                .required(true)
                .build();
        expectedEvents.add(expectedEvent);
    }

    @Test
    public void missingEventsFails() {
        givenAggregate();
        givenExpectedEvents();
        givenAggregateUpdateMessageConsumerForMethod("listenerProducingNoEvent");
        givenMessage();
        whenConsume();
        thenReportFailure();
    }

    private void thenReportFailure() {
        assertTrue(report.isFailed());
    }

    @Test
    public void unexpectedEventFails() {
        givenAggregate();
        givenExpectedEvents();
        givenAggregateUpdateMessageConsumerForMethod("listenerProducingUnexpectedEvent");
        givenMessage();
        whenConsume();
        thenReportFailure();
    }
}
