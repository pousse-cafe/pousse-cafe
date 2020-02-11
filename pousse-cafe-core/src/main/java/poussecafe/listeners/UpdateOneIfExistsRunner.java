package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateOneIfExistsRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateOneIfExistsRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TargetAggregates<K> targetAggregates(M message) {
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        Repository aggregateRepository = aggregateRepository();
        K id = aggregateId(message);
        if(aggregateRepository.existsById(id)) {
            builder.toUpdate(aggregateId(message));
        }
        return builder.build();
    }

    protected abstract K aggregateId(M message);
}
