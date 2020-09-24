package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.DomainException;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.util.ReflectionUtils;

@SuppressWarnings({"rawtypes"})
public class AggregateRootMessageListenerFactory {

    public static class Builder {

        private AggregateRootMessageListenerFactory factory = new AggregateRootMessageListenerFactory();

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

        public AggregateRootMessageListenerFactory build() {
            Objects.requireNonNull(factory.environment);
            Objects.requireNonNull(factory.transactionRunnerLocator);
            Objects.requireNonNull(factory.applicationPerformanceMonitoring);
            return factory;
        }
    }

    public MessageListener buildMessageListener(MessageListenerDefinition definition) {
        Class<? extends AggregateMessageListenerRunner> runnerClass = definition.runner()
                .orElseThrow(() -> new DomainException(definition.method().toString() + " message listeners must have a runner"));
        AggregateMessageListenerRunner runner = ReflectionUtils.newInstance(runnerClass);
        Class aggregateRootClass = definition.method().getDeclaringClass();
        Optional<String> collisionSpace = definition.collisionSpace();
        if(collisionSpace.isEmpty()) {
            collisionSpace = Optional.of(aggregateRootClass.getName());
        }
        return definition.messageListenerBuilder()
                .type(MessageListenerType.AGGREGATE)
                .consumer(buildAggregateRootMessageConsumer(definition, runner))
                .runner(Optional.of(runner))
                .collisionSpace(collisionSpace)
                .aggregateRootClass(Optional.of(aggregateRootClass))
                .build();
    }

    private Environment environment;

    private MessageConsumer buildAggregateRootMessageConsumer(MessageListenerDefinition definition, AggregateMessageListenerRunner runner) {
        Method method = definition.method();
        Class entityClass = method.getDeclaringClass();
        AggregateServices aggregateServices = environment.aggregateServicesOf(entityClass).orElseThrow(PousseCafeException::new);
        return new AggregateUpdateMessageConsumer.Builder()
                .listenerId(definition.shortId())
                .aggregateServices(aggregateServices)
                .transactionRunnerLocator(transactionRunnerLocator)
                .method(method)
                .runner(runner)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .expectedEvents(definition.expectedEvents())
                .hasExpectedEvents(definition.withExpectedEvents())
                .build();
    }

    private TransactionRunnerLocator transactionRunnerLocator;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
