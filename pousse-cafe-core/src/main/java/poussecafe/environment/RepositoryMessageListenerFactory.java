package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRepository;
import poussecafe.exception.PousseCafeException;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"rawtypes"})
public class RepositoryMessageListenerFactory {

    public static class Builder {

        private RepositoryMessageListenerFactory factory = new RepositoryMessageListenerFactory();

        public Builder environment(Environment environment) {
            factory.environment = environment;
            return this;
        }

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            factory.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            factory.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public RepositoryMessageListenerFactory build() {
            Objects.requireNonNull(factory.environment);
            Objects.requireNonNull(factory.transactionRunnerLocator);
            Objects.requireNonNull(factory.applicationPerformanceMonitoring);
            return factory;
        }
    }

    private RepositoryMessageListenerFactory() {

    }

    public MessageListener buildMessageListener(MessageListenerDefinition definition) {
        Method method = definition.method();
        AggregateRepository repository = environment.repository(definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        Class<?> aggregateRootClass = repository.entityClass();
        MethodInvoker invoker = new MethodInvoker.Builder()
                .method(method)
                .target(repository)
                .rethrow(SameOperationException.class)
                .rethrow(RetryOperationException.class)
                .rethrow(OptimisticLockingException.class)
                .build();
        Optional<String> collisionSpace = definition.collisionSpace();
        if(collisionSpace.isEmpty()) {
            collisionSpace = Optional.of(repository.entityClass().getName());
        }
        return definition.messageListenerBuilder()
                .type(MessageListenerType.REPOSITORY)
                .consumer(buildRepositoryMessageConsumer(repository, invoker, definition))
                .collisionSpace(collisionSpace)
                .aggregateRootClass(Optional.of(aggregateRootClass))
                .build();
    }

    private Environment environment;

    private MessageConsumer buildRepositoryMessageConsumer(
            AggregateRepository repository,
            MethodInvoker invoker,
            MessageListenerDefinition definition) {

        if(definition.method().getReturnType() == void.class) {
            return buildLegacyRepositoryMessageConsumer(repository.entityClass(), invoker, definition.shortId());
        } else {
            return buildIdProducingMessageConsumer(repository, definition, invoker);
        }
    }

    private MessageConsumer buildLegacyRepositoryMessageConsumer(Class<?> entityClass, MethodInvoker invoker, String listenerId) {
        return state -> {
            TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
            transactionRunner.runInTransaction(() -> invokeInSpan(invoker, state));
            return MessageListenerConsumptionReport.success(listenerId);
        };
    }

    private TransactionRunnerLocator transactionRunnerLocator;

    private void invokeInSpan(MethodInvoker invoker, MessageListenerGroupConsumptionState state) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(invoker.method().getName());
        try {
            invoker.invoke(state.message().original());
        } finally {
            span.end();
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private MessageConsumer buildIdProducingMessageConsumer(
            AggregateRepository repository,
            MessageListenerDefinition definition,
            MethodInvoker invoker) {
        var method = definition.method();
        var returnType = method.getReturnType();
        var entityClass = repository.entityClass();
        var aggregateServices = environment.aggregateServicesOf(entityClass).orElseThrow();

        if(Iterable.class.isAssignableFrom(returnType)) {
            return deleteSeveralAggregates(definition, invoker, aggregateServices);
        } else {
            return deleteOneOrNoneAggregate(definition, invoker, aggregateServices);
        }
    }

    private MessageConsumer deleteOneOrNoneAggregate(MessageListenerDefinition definition, MethodInvoker invoker, AggregateServices aggregateServices) {
        return new DeleteOneOrNoneAggregateMessageConsumer.Builder()
                .listenerId(definition.shortId())
                .transactionRunnerLocator(transactionRunnerLocator)
                .invoker(invoker)
                .aggregateServices(aggregateServices)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .expectedEvents(definition.expectedEvents())
                .build();
    }

    private MessageConsumer deleteSeveralAggregates(MessageListenerDefinition definition, MethodInvoker invoker, AggregateServices aggregateServices) {
        return new DeleteSeveralAggregatesMessageConsumer.Builder()
                .listenerId(definition.shortId())
                .transactionRunnerLocator(transactionRunnerLocator)
                .invoker(invoker)
                .aggregateServices(aggregateServices)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .expectedEvents(definition.expectedEvents())
                .build();
    }
}
