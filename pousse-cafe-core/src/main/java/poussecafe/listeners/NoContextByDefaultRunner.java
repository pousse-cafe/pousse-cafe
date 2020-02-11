package poussecafe.listeners;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.messaging.Message;

/**
 * @deprecated Implement directly AggregateMessageListenerRunner.
 */
@Deprecated(since = "0.17")
public abstract class NoContextByDefaultRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public Object context(M message, A aggregate) {
        return null;
    }
}
