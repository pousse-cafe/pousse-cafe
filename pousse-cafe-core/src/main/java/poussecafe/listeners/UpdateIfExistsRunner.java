package poussecafe.listeners;

import java.util.Collection;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateIfExistsRunner<M extends Message, K, A extends AggregateRoot<?, ?>>
extends AggregateStateAwareRunner<M, K, A> {

    public UpdateIfExistsRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        Collection<K> ids = aggregateIds(message);
        TargetAggregates.Builder<K> builder = new TargetAggregates.Builder<>();
        for(K id : ids) {
            if(existsById(id)) {
                builder.toUpdate(id);
            }
        }
        return builder.build();
    }

    public abstract Collection<K> aggregateIds(M message);
}
