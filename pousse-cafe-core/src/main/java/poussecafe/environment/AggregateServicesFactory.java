package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
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
        AggregateRepository repository = newRepository(definition.getRepositoryClass());
        AggregateFactory factory = newFactory(definition.getFactoryClass());
        return new AggregateServices(definition.getAggregateRootClass(), repository, factory);
    }

    public AggregateRepository newRepository(Class<? extends AggregateRepository> repositoryClass) {
        AggregateRepository repository = ReflectionUtils.newInstance(repositoryClass);
        Class<?> entityClass = environment.entityOfFactoryOrRepository(repositoryClass);
        repository.setEntityClass(entityClass);
        repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        repository.setApplicationPerformanceMonitoring(applicationPerformanceMonitoring);
        return repository;
    }

    private Object supplyDataAccessImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.entityDataAccessFactory(entityClass);
        return factory.get();
    }

    public AggregateFactory newFactory(Class<? extends AggregateFactory> factoryClass) {
        AggregateFactory factory = ReflectionUtils.newInstance(factoryClass);
        Class<?> entityClass = environment.entityOfFactoryOrRepository(factoryClass);
        factory.setEntityClass(entityClass);
        return factory;
    }
}
