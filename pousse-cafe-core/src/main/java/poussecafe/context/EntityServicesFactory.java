package poussecafe.context;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.domain.AggregateDefinition;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.util.ReflectionUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityServicesFactory {

    public EntityServicesFactory(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Environment environment;

    public EntityServices buildEntityServices(AggregateDefinition definition) {
        Repository repository = newRepository(definition.getRepositoryClass());
        Factory factory = newFactory(definition.getFactoryClass());
        return new EntityServices(definition.getEntityClass(), repository, factory);
    }

    public Repository newRepository(Class<? extends Repository> repositoryClass) {
        Repository repository = ReflectionUtils.newInstance(repositoryClass);
        Class<?> entityClass = environment.getEntityClass(repositoryClass);
        repository.setEntityClass(entityClass);
        repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        return repository;
    }

    private Object supplyDataAccessImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.getEntityDataAccessFactory(entityClass);
        return factory.get();
    }

    public Factory newFactory(Class<? extends Factory> factoryClass) {
        Factory factory = ReflectionUtils.newInstance(factoryClass);
        Class<?> entityClass = environment.getEntityClass(factoryClass);
        factory.setEntityClass(entityClass);
        return factory;
    }
}
