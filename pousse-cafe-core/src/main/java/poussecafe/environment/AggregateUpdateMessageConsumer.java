package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;
import poussecafe.util.MethodInvokerException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AggregateUpdateMessageConsumer implements MessageConsumer {

    public static class Builder {

        private AggregateUpdateMessageConsumer consumer = new AggregateUpdateMessageConsumer();

        public Builder listenerId(String listenerId) {
            consumer.listenerId = listenerId;
            return this;
        }

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
            Objects.requireNonNull(consumer.listenerId);
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
        throw new UnsupportedOperationException();
    }

    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state, Object aggregateId) {
        Message message = state.message().original();
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder(listenerId);
        reportBuilder.logger(state.processorLogger());
        Class entityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(entityClass);
        Repository repository = aggregateServices.repository();
        TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
        reportBuilder.runAndReport(state, aggregateId, () -> updateAggregate(message, repository, transactionRunner, aggregateId));
        return reportBuilder.build();
    }

    private String listenerId;

    private AggregateRoot updateAggregate(Message message, Repository repository, TransactionRunner transactionRunner, Object id) {
        AggregateReference reference = new AggregateReference();
        transactionRunner.runInTransaction(() -> updateInSpan(message, repository, id, reference));
        return reference.aggregate;
    }

    private class AggregateReference {

        AggregateRoot aggregate;
    }

    private void updateInSpan(Message message, Repository repository, Object id, AggregateReference reference) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(method.getName());
        try {
            AggregateRoot targetAggregateRoot = repository.get(id);
            targetAggregateRoot.context(runner.context(message, targetAggregateRoot));
            MethodInvoker invoker = new MethodInvoker.Builder()
                    .method(method)
                    .target(targetAggregateRoot)
                    .rethrow(SameOperationException.class)
                    .build();
            invoker.invoke(message);
            repository.update(targetAggregateRoot);
            reference.aggregate = targetAggregateRoot;
        } catch(SameOperationException | OptimisticLockingException e) {
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

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private AggregateMessageListenerRunner runner;

    public AggregateMessageListenerRunner runner() {
        return runner;
    }

    private Method method;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;
}
