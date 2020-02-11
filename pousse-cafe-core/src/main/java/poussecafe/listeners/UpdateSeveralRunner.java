package poussecafe.listeners;

import java.util.Collection;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateSeveralRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        return new TargetAggregates.Builder<K>()
                .toUpdate(aggregateIds(message))
                .build();
    }

    public abstract Collection<K> aggregateIds(M message);
}
