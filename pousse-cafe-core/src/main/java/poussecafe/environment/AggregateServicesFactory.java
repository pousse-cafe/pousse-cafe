package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.runtime.AggregateServices;
import poussecafe.util.ReflectionUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AggregateServicesFactory {

    public AggregateServicesFactory(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Environment environment;

    public AggregateServices buildEntityServices(AggregateDefinition definition) {
        Repository repository = newRepository(definition.getRepositoryClass());
        Factory factory = newFactory(definition.getFactoryClass());
        return new AggregateServices(definition.getEntityClass(), repository, factory);
    }

    public Repository newRepository(Class<? extends Repository> repositoryClass) {
        Repository repository = ReflectionUtils.newInstance(repositoryClass);
        Class<?> entityClass = environment.entityOfFactoryOrRepository(repositoryClass);
        repository.setEntityClass(entityClass);
        repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        return repository;
    }

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
