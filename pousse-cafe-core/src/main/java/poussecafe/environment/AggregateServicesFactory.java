package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.Factory;
import poussecafe.domain.MessageCollectionValidator;
import poussecafe.domain.Repository;
import poussecafe.util.ReflectionUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AggregateServicesFactory {

    public static class Builder {

        private AggregateServicesFactory factory = new AggregateServicesFactory();

        public Builder environment(Environment environment) {
            factory.environment = environment;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            factory.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public AggregateServicesFactory build() {
            Objects.requireNonNull(factory.environment);
            Objects.requireNonNull(factory.applicationPerformanceMonitoring);
            return factory;
        }
    }

    private AggregateServicesFactory() {

    }

    private Environment environment;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    public AggregateServices buildEntityServices(AggregateDefinition definition) {
        Repository repository = newRepository(definition.getRepositoryClass());
        Factory factory = newFactory(definition.getFactoryClass());
        return new AggregateServices(definition.getAggregateRootClass(), repository, factory);
    }

    public Repository newRepository(Class<? extends Repository> repositoryClass) {
        Repository repository = ReflectionUtils.newInstance(repositoryClass);
        Class<?> entityClass = environment.entityOfFactoryOrRepository(repositoryClass);
        repository.setEntityClass(entityClass);
        repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        repository.setMessageCollectionValidator(messageCollectionValidator);
        repository.setApplicationPerformanceMonitoring(applicationPerformanceMonitoring);
        return repository;
    }

    private MessageCollectionValidator messageCollectionValidator = new MessageCollectionValidator();

    private Object supplyDataAccessImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.entityDataAccessFactory(entityClass);
        return factory.get();
    }

    public Factory newFactory(Class<? extends Factory> factoryClass) {
        Factory factory = ReflectionUtils.newInstance(factoryClass);
        Class<?> entityClass = environment.entityOfFactoryOrRepository(factoryClass);
        factory.setEntityClass(entityClass);
        return factory;
    }
}
