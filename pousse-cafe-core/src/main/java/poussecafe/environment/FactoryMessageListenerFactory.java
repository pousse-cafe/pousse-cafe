package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.Factory;
import poussecafe.exception.PousseCafeException;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FactoryMessageListenerFactory {

    public static class Builder {

        private FactoryMessageListenerFactory factory = new FactoryMessageListenerFactory();

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

        public FactoryMessageListenerFactory build() {
            Objects.requireNonNull(factory.environment);
            Objects.requireNonNull(factory.transactionRunnerLocator);
            Objects.requireNonNull(factory.applicationPerformanceMonitoring);
            return factory;
        }
    }

    public MessageListener buildMessageListener(MessageListenerDefinition definition) {
        Factory target = environment.factory(definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        Optional<String> collisionSpace = definition.collisionSpace();
        if(collisionSpace.isEmpty()) {
            collisionSpace = Optional.of(target.entityClass().getName());
        }
        return definition.messageListenerBuilder()
                .priority(MessageListenerType.FACTORY)
                .consumer(buildFactoryListenerConsumer(target, definition))
                .collisionSpace(collisionSpace)
                .aggregateRootClass(Optional.of(target.entityClass()))
                .build();
    }

    private Environment environment;

    private MessageConsumer buildFactoryListenerConsumer(Factory factory, MessageListenerDefinition definition) {
        Class entityClass = factory.entityClass();
        AggregateServices aggregateServices = environment.aggregateServicesOf(entityClass).orElseThrow(PousseCafeException::new);
        Method method = definition.method();
        MethodInvoker invoker = new MethodInvoker.Builder()
                .method(method)
                .target(factory)
                .rethrow(SameOperationException.class)
                .rethrow(OptimisticLockingException.class)
                .build();
        Class<?> returnType = method.getReturnType();
        if(entityClass.isAssignableFrom(returnType) || returnType.isAssignableFrom(Optional.class)) {
            return addSingleCreatedAggregate(invoker, aggregateServices);
        } else if(Iterable.class.isAssignableFrom(returnType)) {
            return addIterableCreatedAggregates(invoker, aggregateServices);
        } else {
            throw new PousseCafeException("Method " + method.getName() + " of " + factory.getClass().getName() + " is not a valid factory message listener");
        }
    }

    private MessageConsumer addSingleCreatedAggregate(MethodInvoker invoker,
            AggregateServices aggregateServices) {
        return new SingleAggregateCreationMessageConsumer.Builder()
                .transactionRunnerLocator(transactionRunnerLocator)
                .invoker(invoker)
                .aggregateServices(aggregateServices)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .build();
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private TransactionRunnerLocator transactionRunnerLocator;

    private MessageConsumer addIterableCreatedAggregates(MethodInvoker invoker,
            AggregateServices aggregateServices) {
        return new SeveralAggregatesCreationMessageConsumer.Builder()
                .transactionRunnerLocator(transactionRunnerLocator)
                .invoker(invoker)
                .aggregateServices(aggregateServices)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .build();
    }
}
