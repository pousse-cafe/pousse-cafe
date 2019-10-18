package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.DomainException;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
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
        Optional<String> collisionSpace = definition.collisionSpace();
        if(collisionSpace.isEmpty()) {
            collisionSpace = Optional.of(definition.method().getDeclaringClass().getName());
        }
        return definition.messageListenerBuilder()
                .priority(MessageListenerPriority.AGGREGATE)
                .consumer(buildAggregateRootMessageConsumer(definition, runner))
                .runner(Optional.of(runner))
                .collisionSpace(collisionSpace)
                .build();
    }

    private Environment environment;

    private Consumer<Message> buildAggregateRootMessageConsumer(MessageListenerDefinition definition, AggregateMessageListenerRunner runner) {
        Method method = definition.method();
        Class entityClass = method.getDeclaringClass();
        AggregateServices aggregateServices = environment.aggregateServicesOf(entityClass).orElseThrow(PousseCafeException::new);
        return new AggregateUpdateMessageConsumer.Builder()
                .aggregateServices(aggregateServices)
                .transactionRunnerLocator(transactionRunnerLocator)
                .method(method)
                .runner(runner)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .build();
    }

    private TransactionRunnerLocator transactionRunnerLocator;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
