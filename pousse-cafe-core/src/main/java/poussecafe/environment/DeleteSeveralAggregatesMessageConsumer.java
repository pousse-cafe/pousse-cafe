package poussecafe.environment;

import java.util.List;
import java.util.Objects;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;
import poussecafe.util.MethodInvokerException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DeleteSeveralAggregatesMessageConsumer implements MessageConsumer {

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder(listenerId);
        reportBuilder.logger(state.processorLogger());
        Class entityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(entityClass);
        Message message = state.message().original();
        deleteAggregates(state, message, reportBuilder);
        return reportBuilder.build();
    }

    private String listenerId;

    private void deleteAggregates(MessageListenerGroupConsumptionState state, Message message, MessageListenerConsumptionReport.Builder reportBuilder) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            Class entityClass = aggregateServices.aggregateRootEntityClass();
            TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
            AggregateRepository repository = aggregateServices.repository();

            Iterable<AggregateRoot> aggregates = (Iterable<AggregateRoot>) invoker.invoke(message);
            for(Object identifier : aggregates) {
                reportBuilder.runAndReport(state, identifier, () -> deleteAggregate(transactionRunner, repository, identifier));
            }
        } catch(SameOperationException | RetryOperationException e) {
            throw e;
        } catch(MethodInvokerException e) {
            span.captureException(e.getCause());
            throw e;
        } catch(Exception e) {
            span.captureException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    private MethodInvoker invoker;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;

    private void deleteAggregate(TransactionRunner transactionRunner,
            AggregateRepository repository,
            Object identifier) {
        transactionRunner.runInTransaction(() -> {
            var aggregate = repository.getOptional(identifier);
            if(aggregate.isPresent()) {
                var presentAggregate = (AggregateRoot) aggregate.get();
                presentAggregate.addExecutedListener(listenerId);
                presentAggregate.addExpectedEvents(expectedEvents);
                repository.delete(presentAggregate);
            }
        });
    }

    private List<ExpectedEvent> expectedEvents;

    public static class Builder {

        private DeleteSeveralAggregatesMessageConsumer consumer = new DeleteSeveralAggregatesMessageConsumer();

        public Builder listenerId(String listenerId) {
            consumer.listenerId = listenerId;
            return this;
        }

        public Builder invoker(MethodInvoker invoker) {
            consumer.invoker = invoker;
            return this;
        }

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            consumer.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        public Builder aggregateServices(AggregateServices aggregateServices) {
            consumer.aggregateServices = aggregateServices;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            consumer.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder expectedEvents(List<ExpectedEvent> expectedEvents) {
            consumer.expectedEvents = expectedEvents;
            return this;
        }

        public DeleteSeveralAggregatesMessageConsumer build() {
            Objects.requireNonNull(consumer.listenerId);
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.invoker);
            return consumer;
        }
    }

    private DeleteSeveralAggregatesMessageConsumer() {

    }
}
