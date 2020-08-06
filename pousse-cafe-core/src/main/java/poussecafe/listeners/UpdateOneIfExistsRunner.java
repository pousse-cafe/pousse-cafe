package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateOneIfExistsRunner<M extends Message, K, A extends AggregateRoot<?, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateOneIfExistsRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        K id = aggregateId(message);
        if(existsById(id)) {
            builder.toUpdate(id);
        }
        return builder.build();
    }

    protected abstract K aggregateId(M message);
}
