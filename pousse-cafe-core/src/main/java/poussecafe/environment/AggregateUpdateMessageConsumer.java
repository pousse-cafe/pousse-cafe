package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AggregateUpdateMessageConsumer implements MessageConsumer {

    public static class Builder {

        private AggregateUpdateMessageConsumer consumer = new AggregateUpdateMessageConsumer();

        public Builder method(Method method) {
            consumer.method = method;
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

        public Builder runner(AggregateMessageListenerRunner runner) {
            consumer.runner = runner;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            consumer.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public AggregateUpdateMessageConsumer build() {
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.method);
            Objects.requireNonNull(consumer.applicationPerformanceMonitoring);
            return consumer;
        }
    }

    private AggregateUpdateMessageConsumer() {

    }

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        Message message = state.message().original();
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder();
        Class entityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(entityClass);
        Set targetAggregatesIds = runner.targetAggregatesIds(message);
        Repository repository = aggregateServices.repository();
        for(Object id : targetAggregatesIds) {
            reportBuilder.runAndReport(state, id, () -> updateAggregate(message, repository, entityClass, id));
        }
        return reportBuilder.build();
    }

    private void updateAggregate(Message message, Repository repository, Class entityClass, Object id) {
        TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
        transactionRunner.runInTransaction(() -> {
            AggregateRoot targetAggregateRoot = repository.get(id);
            targetAggregateRoot.context(runner.context(message, targetAggregateRoot));
            MethodInvoker invoker = new MethodInvoker.Builder()
                    .method(method)
                    .target(targetAggregateRoot)
                    .rethrow(SameOperationException.class)
                    .build();
            updateInSpan(message, invoker);
            repository.update(targetAggregateRoot);
        });
    }

    private void updateInSpan(Message message, MethodInvoker invoker) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            invoker.invoke(message);
        } catch(Exception e) {
            span.captureException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private AggregateMessageListenerRunner runner;

    private Method method;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;
}
