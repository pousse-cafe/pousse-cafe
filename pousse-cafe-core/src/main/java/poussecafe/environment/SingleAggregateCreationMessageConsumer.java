package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SingleAggregateCreationMessageConsumer implements MessageConsumer {

    public static class Builder {

        private SingleAggregateCreationMessageConsumer consumer = new SingleAggregateCreationMessageConsumer();

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
    public MessageConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        MessageConsumptionReport.Builder reportBuilder = new MessageConsumptionReport.Builder();
        Class aggregateRootEntityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(aggregateRootEntityClass);
        Message message = state.message().original();
        createAggregate(state, reportBuilder, aggregateRootEntityClass, message);
        return reportBuilder.build();
    }

    private void createAggregate(MessageListenerGroupConsumptionState state, MessageConsumptionReport.Builder reportBuilder, Class aggregateRootEntityClass, Message message) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            AggregateRoot aggregate = newAggregate(message);
            if(aggregate != null) {
                Repository repository = aggregateServices.repository();
                TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(aggregateRootEntityClass);
                reportBuilder.runAndReport(state, aggregate.attributes().identifier().value(), () -> addCreatedAggregate(transactionRunner, repository, aggregate));
            }
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
