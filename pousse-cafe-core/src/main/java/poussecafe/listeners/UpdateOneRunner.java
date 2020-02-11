package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateOneRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        return new TargetAggregates.Builder<K>()
                .toUpdate(aggregateId(message))
                .build();
    }

    protected abstract K aggregateId(M message);
}
