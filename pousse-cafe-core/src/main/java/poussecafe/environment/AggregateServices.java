package poussecafe.environment;

import java.util.Objects;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregateServices {

    private Class aggregateRootEntityClass;

    private AggregateRepository repository;

    private AggregateFactory factory;

    public AggregateServices(Class entityClass, AggregateRepository repository,
            AggregateFactory factory) {
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

    public <R extends AggregateRepository<?, ?, ?>> R repository() {
        return (R) repository;
    }

    private void repository(AggregateRepository repository) {
        Objects.requireNonNull(repository);
        this.repository = repository;
    }

    public <F extends AggregateFactory<?, ?, ?>> F factory() {
        return (F) factory;
    }

    private void factory(AggregateFactory factory) {
        Objects.requireNonNull(factory);
        this.factory = factory;
    }

}
