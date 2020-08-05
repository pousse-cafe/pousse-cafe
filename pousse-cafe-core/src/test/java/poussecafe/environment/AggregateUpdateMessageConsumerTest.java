package poussecafe.environment;

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
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.NoTransactionRunner;
import poussecafe.testmodule.SimpleMessage;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        whenConsume();
        thenReportSkip();
    }

    private void givenAggregate() {
        aggregateRoot = new Aggregate();
    }

    private Aggregate aggregateRoot;

    public static class Aggregate extends AggregateRoot<String, EntityAttributes<String>> {

        public void skip(Message message) {
            throw new SameOperationException();
        }

        public void retry(Message message) {
            throw new RetryOperationException();
        }
    }

    private void givenAggregateUpdateMessageConsumerForMethod(String methodName) {
        repository = mock(Repository.class);
        when(repository.get("id")).thenReturn(aggregateRoot);
        Factory factory = mock(Factory.class);
        AggregateServices aggregateServices = new AggregateServices(Aggregate.class, repository, factory);

        applicationPerformanceMonitoring = mock(ApplicationPerformanceMonitoring.class);
        ApmSpan currentSpan = mock(ApmSpan.class);
        when(applicationPerformanceMonitoring.currentSpan()).thenReturn(currentSpan);
        newSpan = mock(ApmSpan.class);
        when(currentSpan.startSpan()).thenReturn(newSpan);

        AggregateMessageListenerRunner runner = mock(AggregateMessageListenerRunner.class);
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

    private AggregateUpdateMessageConsumer consumer;

    private void whenConsume() {
        SimpleMessage message = new SimpleMessage();
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
        whenConsume();
        thenReportRetry();
    }

    private void thenReportRetry() {
        assertTrue(report.mustRetry());
    }
}
