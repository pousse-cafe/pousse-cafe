package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateOrCreateOneRunner<M extends Message, K, A extends AggregateRoot<?, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateOrCreateOneRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        K id = aggregateId(message);
        if(existsById(id)) {
            builder.toUpdate(id);
        } else {
            builder.toCreate(id);
        }
        return builder.build();
    }

    protected abstract K aggregateId(M message);
}
