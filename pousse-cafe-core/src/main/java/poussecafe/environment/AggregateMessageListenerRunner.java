package poussecafe.environment;

import java.util.Set;

import static java.util.Collections.emptySet;

public interface AggregateMessageListenerRunner<M, K, A> {

    /**
     * @deprecated Use targetAggregates instead.
     */
    @Deprecated(since = "0.15.0")
    default Set<K> targetAggregatesIds(M message) {
        return emptySet();
    }

    /**
     * Default implementation will be removed with targetAggregatesIds. This gives some time for API consumers
     * to adapt their implementations.
     */
    default TargetAggregates<K> targetAggregates(M message) {
        return new TargetAggregates.Builder<K>()
                .toUpdate(targetAggregatesIds(message))
                .build();
    }

    /**
     * No context by default.
     */
    default Object context(M message, A aggregate) {
        return null;
    }
}
