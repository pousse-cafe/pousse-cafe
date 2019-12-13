package poussecafe.discovery;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.messaging.Message;

/**
 * @deprecated use NoContextByDefaultRunner instead. You may also turn to more specific implementations like
 * AlwaysUpdateRunner, AlwaysUpdateOneRunner, UpdateOrCreateRunner or UpdateOrCreateOneRunner.
 */
@Deprecated(since = "0.16.0")
public abstract class DefaultAggregateMessageListenerRunner<M extends Message, K, A extends AggregateRoot<K, ?>> implements AggregateMessageListenerRunner<M, K, A> {

    @Override
    public Object context(M message, A aggregate) {
        return null;
    }
}
