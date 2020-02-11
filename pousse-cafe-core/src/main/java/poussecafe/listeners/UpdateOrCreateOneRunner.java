package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class UpdateOrCreateOneRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateOrCreateOneRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        Repository aggregateRepository = aggregateRepository();
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        K id = aggregateId(message);
        if(aggregateRepository.existsById(id)) {
            builder.toUpdate(id);
        } else {
            builder.toCreate(id);
        }
        return builder.build();
    }

    protected abstract K aggregateId(M message);
}
