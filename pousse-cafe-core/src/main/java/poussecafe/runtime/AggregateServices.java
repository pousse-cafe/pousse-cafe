package poussecafe.runtime;

import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class AggregateServices {

    private Class<?> entityClass;

    private Repository<?, ?, ?> repository;

    private Factory<?, ?, ?> factory;

    public AggregateServices(Class<?> entityClass, Repository<?, ?, ?> repository,
            Factory<?, ?, ?> factory) {
        setEntityClass(entityClass);
        setRepository(repository);
        setFactory(factory);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    private void setEntityClass(Class<?> entityClass) {
        checkThat(value(entityClass).notNull().because("Entity class cannot be null"));
        this.entityClass = entityClass;
    }

    public Repository<?, ?, ?> getRepository() {
        return repository;
    }

    private void setRepository(Repository<?, ?, ?> repository) {
        checkThat(value(repository).notNull().because("Repository cannot be null"));
        this.repository = repository;
    }

    public Factory<?, ?, ?> getFactory() {
        return factory;
    }

    private void setFactory(Factory<?, ?, ?> factory) {
        checkThat(value(factory).notNull().because("Factory cannot be null"));
        this.factory = factory;
    }

}
