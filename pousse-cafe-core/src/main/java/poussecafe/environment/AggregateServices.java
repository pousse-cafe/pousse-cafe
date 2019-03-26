package poussecafe.environment;

import java.util.Objects;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

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
        Objects.requireNonNull(entityClass);
        this.entityClass = entityClass;
    }

    public Repository<?, ?, ?> getRepository() {
        return repository;
    }

    private void setRepository(Repository<?, ?, ?> repository) {
        Objects.requireNonNull(repository);
        this.repository = repository;
    }

    public Factory<?, ?, ?> getFactory() {
        return factory;
    }

    private void setFactory(Factory<?, ?, ?> factory) {
        Objects.requireNonNull(factory);
        this.factory = factory;
    }

}
