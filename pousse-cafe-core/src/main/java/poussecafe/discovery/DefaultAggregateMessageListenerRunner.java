package poussecafe.discovery;

import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.messaging.Message;

/**
 * @deprecated directly implement AggregateMessageListenerRunner instead.
 * You may also turn yourself to more specific implementations like
 * UpdateSeveralRunner, AlwaysUpdateOneRunner, UpdateOneRunner, UpdateOrCreateRunner, UpdateOrCreateOneRunner,
 * UpdateIfExistsRunner or UpdateOneIfExistsRunner.
 */
@Deprecated(since = "0.16.0")
public abstract class DefaultAggregateMessageListenerRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

}
