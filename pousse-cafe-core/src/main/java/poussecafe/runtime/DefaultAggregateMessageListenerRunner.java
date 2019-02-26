package poussecafe.runtime;

import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.Message;

public abstract class DefaultAggregateMessageListenerRunner<M extends Message, K, A extends AggregateRoot<K, ?>> implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public Object context(M message, A aggregate) {
        return null;
    }
}
