package poussecafe.environment;

import java.util.Set;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

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
     * Builds a context object for given aggregate in function of received message.
     *
     * No context by default.
     */
    default Object context(M message, A aggregate) {
        return null;
    }

    /**
     * Checks that received message is valid from a chronological point of view i.e. it is not an anachronism by either
     * arriving too late (the effect of the message's consumption is already visible) or too soon (another message
     * should have been consumed first).
     *
     * A message arriving too late must be ignored. This is achieved by letting this method throw a
     * SameOperationException.
     *
     * The consumption of a message arriving too soon must be retried later. This is achieved by letting this method
     * throw a RetryOperationException.
     *
     * @param message The message
     * @param targetAggregateRoot The aggregate telling if the message is anachronical or not.
     *
     * @throws SameOperationException
     * @throws RetryOperationException
     */
    default void validChronologyOrElseThrow(M message, A targetAggregateRoot) {
        // Optional check
    }
}
