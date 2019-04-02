package poussecafe.environment;

import java.util.Set;

public interface AggregateMessageListenerRunner<M, K, A> {

    Set<K> targetAggregatesIds(M message);

    Object context(M message, A aggregate);
}
