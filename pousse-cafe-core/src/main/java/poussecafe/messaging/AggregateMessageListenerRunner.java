package poussecafe.messaging;

import java.util.Set;

public interface AggregateMessageListenerRunner<M, K, A> {

    Set<K> targetAggregatesKeys(M message);

    Object context(M message, A aggregate);
}
