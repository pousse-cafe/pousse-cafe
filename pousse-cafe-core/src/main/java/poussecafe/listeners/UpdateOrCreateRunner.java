package poussecafe.listeners;

import java.util.Collection;
import java.util.Objects;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.environment.Environment;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class UpdateOrCreateRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
extends NoContextByDefaultRunner<M, K, A> {

    public UpdateOrCreateRunner(Class<A> aggregateRootClass) {
        Objects.requireNonNull(aggregateRootClass);
        this.aggregateRootClass = aggregateRootClass;
    }

    private Class<A> aggregateRootClass;

    private Environment environment;

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        Collection<K> ids = aggregateIds(message);
        Repository aggregateRepository = aggregateRepository();
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        for(K id : ids) {
            if(aggregateRepository.existsById(id)) {
                builder.toUpdate(id);
            } else {
                builder.toCreate(id);
            }
        }
        return builder.build();
    }

    private Repository aggregateRepository() {
        if(repository == null) {
            repository = environment.repositoryOf(aggregateRootClass).orElseThrow();
        }
        return repository;
    }

    private Repository repository;

    public abstract Collection<K> aggregateIds(M message);
}
