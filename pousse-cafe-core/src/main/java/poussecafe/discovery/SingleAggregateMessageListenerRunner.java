package poussecafe.discovery;

import java.util.Set;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.Message;

import static poussecafe.collection.Collections.asSet;

public abstract class SingleAggregateMessageListenerRunner<M extends Message, K, A extends AggregateRoot<K, ?>> extends DefaultAggregateMessageListenerRunner<M, K, A> {

    @Override
    public Set<K> targetAggregatesKeys(M message) {
        return asSet(targetAggregateKey(message));
    }

    public abstract K targetAggregateKey(M message);
}
