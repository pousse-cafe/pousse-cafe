package poussecafe.environment;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class DeleteOneOrNoneAggregateMessageConsumer implements MessageConsumer {

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder(listenerId);
        reportBuilder.logger(state.processorLogger());
        Class aggregateRootEntityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(aggregateRootEntityClass);
        Message message = state.message().original();
        deleteAggregate(state, reportBuilder, message);
        return reportBuilder.build();
    }

    private String listenerId;

    private void deleteAggregate(MessageListenerGroupConsumptionState state, MessageListenerConsumptionReport.Builder reportBuilder, Message message) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            Object identifier = aggregateIdentifier(message);
            if(identifier != null) {
                Class entityClass = aggregateServices.aggregateRootEntityClass();
                AggregateRepository repository = aggregateServices.repository();
                TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
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

    private Object aggregateIdentifier(Message message) {
        Object result = invoker.invoke(message);
        Object identifier;
        if(result instanceof Optional) {
            Optional optional = (Optional) result;
            identifier = optional.orElse(null);
        } else {
            identifier = result;
        }
        return identifier;
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

        private DeleteOneOrNoneAggregateMessageConsumer consumer = new DeleteOneOrNoneAggregateMessageConsumer();

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

        public DeleteOneOrNoneAggregateMessageConsumer build() {
            Objects.requireNonNull(consumer.listenerId);
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.invoker);
            Objects.requireNonNull(consumer.applicationPerformanceMonitoring);
            return consumer;
        }
    }

    private DeleteOneOrNoneAggregateMessageConsumer() {

    }
}
