package poussecafe.environment;

import java.util.Objects;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SeveralAggregatesCreationMessageConsumer implements MessageConsumer {

    public static class Builder {

        private SeveralAggregatesCreationMessageConsumer consumer = new SeveralAggregatesCreationMessageConsumer();

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

        public SeveralAggregatesCreationMessageConsumer build() {
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.invoker);
            return consumer;
        }
    }

    private SeveralAggregatesCreationMessageConsumer() {

    }

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder();
        Class entityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(entityClass);
        Message message = state.message().original();
        createAggregates(state, message, reportBuilder);
        return reportBuilder.build();
    }

    private void createAggregates(MessageListenerGroupConsumptionState state, Message message, MessageListenerConsumptionReport.Builder reportBuilder) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            Class entityClass = aggregateServices.aggregateRootEntityClass();
            TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
            Repository repository = aggregateServices.repository();

            Iterable<AggregateRoot> aggregates = (Iterable<AggregateRoot>) invoker.invoke(message);
            for(AggregateRoot aggregate : aggregates) {
                reportBuilder.runAndReport(state, aggregate.attributes().identifier().value(), () -> addCreatedAggregate(transactionRunner, repository, aggregate));
            }
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

    private void addCreatedAggregate(TransactionRunner transactionRunner,
            Repository repository,
            AggregateRoot aggregate) {
        transactionRunner.runInTransaction(() -> repository.add(aggregate));
    }
}
