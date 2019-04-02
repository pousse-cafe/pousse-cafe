package poussecafe.discovery;

import java.util.Set;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.Message;

import static poussecafe.collection.Collections.asSet;

public abstract class SingleAggregateMessageListenerRunner<M extends Message, K, A extends AggregateRoot<K, ?>> extends DefaultAggregateMessageListenerRunner<M, K, A> {

    @Override
    public Set<K> targetAggregatesIds(M message) {
        return asSet(targetAggregateId(message));
    }

    public abstract K targetAggregateId(M message);
}
