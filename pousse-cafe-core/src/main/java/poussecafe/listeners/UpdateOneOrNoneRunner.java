package poussecafe.listeners;

import java.util.Optional;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;

public abstract class UpdateOneOrNoneRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public TargetAggregates<K> targetAggregates(M message) {
        var builder = new TargetAggregates.Builder<K>();
        Optional<K> id = aggregateId(message);
        if(id.isPresent()) {
            builder.toUpdate(id.get());
        }
        return builder.build();
    }

    protected abstract Optional<K> aggregateId(M message);
}
