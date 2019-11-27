package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;
import poussecafe.util.MethodInvokerException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SingleAggregateCreationMessageConsumer implements MessageConsumer {

    public static class Builder {

        private SingleAggregateCreationMessageConsumer consumer = new SingleAggregateCreationMessageConsumer();

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

        public SingleAggregateCreationMessageConsumer build() {
            Objects.requireNonNull(consumer.listenerId);
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.invoker);
            Objects.requireNonNull(consumer.applicationPerformanceMonitoring);
            return consumer;
        }
    }

    private SingleAggregateCreationMessageConsumer() {

    }

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder(listenerId);
        reportBuilder.logger(state.processorLogger());
        Class aggregateRootEntityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(aggregateRootEntityClass);
        Message message = state.message().original();
        createAggregate(state, reportBuilder, message);
        return reportBuilder.build();
    }

    private String listenerId;

    private void createAggregate(MessageListenerGroupConsumptionState state, MessageListenerConsumptionReport.Builder reportBuilder, Message message) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            AggregateRoot aggregate = newAggregate(message);
            if(aggregate != null) {
                Class entityClass = aggregateServices.aggregateRootEntityClass();
                Repository repository = aggregateServices.repository();
                TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
                reportBuilder.runAndReport(state, aggregate.attributes().identifier().value(), () -> addCreatedAggregate(transactionRunner, repository, aggregate));
            }
        } catch(SameOperationException e) {
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

    private AggregateRoot newAggregate(Message message) {
        Object result = invoker.invoke(message);
        AggregateRoot aggregate;
        if(result instanceof Optional) {
            Optional<? extends AggregateRoot> optional = (Optional<? extends AggregateRoot>) result;
            aggregate = optional.orElse(null);
        } else {
            aggregate = (AggregateRoot) result;
        }
        return aggregate;
    }

    private MethodInvoker invoker;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;

    private void addCreatedAggregate(TransactionRunner transactionRunner,
            Repository repository,
            AggregateRoot aggregate) {
        transactionRunner.runInTransaction(() -> repository.add(aggregate));
    }
}
