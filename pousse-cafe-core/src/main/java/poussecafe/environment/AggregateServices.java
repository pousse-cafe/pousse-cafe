package poussecafe.environment;

import java.util.Objects;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregateServices {

    private Class aggregateRootEntityClass;

    private Repository repository;

    private Factory factory;

    public AggregateServices(Class entityClass, Repository repository,
            Factory factory) {
        aggregateRootEntityClass(entityClass);
        repository(repository);
        factory(factory);
    }

    public Class aggregateRootEntityClass() {
        return aggregateRootEntityClass;
    }

    private void aggregateRootEntityClass(Class<?> entityClass) {
        Objects.requireNonNull(entityClass);
        aggregateRootEntityClass = entityClass;
    }

    public <R extends Repository<?, ?, ?>> R repository() {
        return (R) repository;
    }

    private void repository(Repository repository) {
        Objects.requireNonNull(repository);
        this.repository = repository;
    }

    public <F extends Factory<?, ?, ?>> F factory() {
        return (F) factory;
    }

    private void factory(Factory factory) {
        Objects.requireNonNull(factory);
        this.factory = factory;
    }

}
