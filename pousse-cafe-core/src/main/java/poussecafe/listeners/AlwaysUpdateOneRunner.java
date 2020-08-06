package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

/**
 * @deprecated Use UpdateOneRunner instead.
 */
@Deprecated(since = "0.17")
public abstract class AlwaysUpdateOneRunner<M extends Message, K, A extends AggregateRoot<?, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        return new TargetAggregates.Builder<K>()
                .toUpdate(aggregateId(message))
                .build();
    }

    protected abstract K aggregateId(M message);
}
