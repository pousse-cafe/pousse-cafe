package poussecafe.environment;

import java.util.Objects;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.TransactionRunnerLocator;

public class MessageListenerFactory {

    public static class Builder {

        private MessageListenerFactory factory = new MessageListenerFactory();

        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        private Environment environment;

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            this.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        private TransactionRunnerLocator transactionRunnerLocator;

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            this.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

        public MessageListenerFactory build() {
            Objects.requireNonNull(environment);
            Objects.requireNonNull(transactionRunnerLocator);

            factory.domainProcessMessageListenerFactory = new DomainProcessMessageListenerFactory(environment);
            factory.factoryMessageListenerFactory = new FactoryMessageListenerFactory.Builder()
                    .environment(environment)
                    .transactionRunnerLocator(transactionRunnerLocator)
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .build();
            factory.repositoryMessageListenerFactory = new RepositoryMessageListenerFactory.Builder()
                    .environment(environment)
                    .transactionRunnerLocator(transactionRunnerLocator)
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .build();
            factory.aggregateRootMessageListenerFactory = new AggregateRootMessageListenerFactory.Builder()
                    .environment(environment)
                    .transactionRunnerLocator(transactionRunnerLocator)
                    .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                    .build();

            return factory;
        }
    }

    public MessageListener build(MessageListenerDefinition definition) {
        Class<?> declaringClass = definition.method().getDeclaringClass();
        if(DomainProcess.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return domainProcessMessageListenerFactory.buildMessageListener(definition);
        } else if(Factory.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return factoryMessageListenerFactory.buildMessageListener(definition);
        } else if(Repository.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return repositoryMessageListenerFactory.buildMessageListener(definition);
        } else if(AggregateRoot.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return aggregateRootMessageListenerFactory.buildMessageListener(definition);
        } else {
            throw new UnsupportedOperationException("Unsupported declaring class " + declaringClass);
        }
    }

    private DomainProcessMessageListenerFactory domainProcessMessageListenerFactory;

    private FactoryMessageListenerFactory factoryMessageListenerFactory;

    private RepositoryMessageListenerFactory repositoryMessageListenerFactory;

    private AggregateRootMessageListenerFactory aggregateRootMessageListenerFactory;
}
