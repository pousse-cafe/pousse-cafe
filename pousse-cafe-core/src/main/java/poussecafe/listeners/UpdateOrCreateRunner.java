package poussecafe.listeners;

import java.util.Collection;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class UpdateOrCreateRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateOrCreateRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

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

    public abstract Collection<K> aggregateIds(M message);
}
