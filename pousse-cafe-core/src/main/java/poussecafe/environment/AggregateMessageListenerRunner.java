package poussecafe.environment;

import java.util.Optional;
import java.util.Set;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

import static java.util.Collections.emptySet;

public interface AggregateMessageListenerRunner<M, K, A> {

    /**
     * @deprecated Use #targetAggregates(M) instead.
     */
    @Deprecated(since = "0.15.0")
    default Set<K> targetAggregatesIds(M message) {
        return emptySet();
    }

    /**
     * Provides a collection of main or secondary identifiers for aggregates to update or to create if the aggregate
     * does not exist yet (then, a Factory listener for this message must exist).
     *
     * If an aggregate to update does not actually exist, an error is reported unless this is a chronology issue (see
     * validChronologyOrElseThrow(M, K, Optional<A>)).
     */
    default TargetAggregates<K> targetAggregates(M message) {
        // The default implementation will be removed with targetAggregatesIds. This gives some time for API consumers
        // to adapt their implementations.
        return new TargetAggregates.Builder<K>()
                .toUpdate(targetAggregatesIds(message))
                .build();
    }

    /**
     * Builds a context object for given aggregate in function of the received message.
     *
     * There is no context by default. Actually, using context objects should be considered as a bad practice and
     * reserved to the very special cases where no other solution (e.g. denormalization) exists.
     */
    default Object context(M message, A aggregate) {
        return null;
    }

    /**
     * Checks that received message is valid from a chronological point of view i.e. it is not an anachronism by either
     * arriving "too late" (the effect of the message's consumption is already visible) or "too soon" (another message
     * should have been consumed first).
     *
     * A message arriving too late must be ignored. This is achieved by letting this method throw a
     * SameOperationException.
     *
     * The consumption of a message arriving too soon must be retried later. This is achieved by letting this method
     * throw a RetryOperationException.
     *
     * Note that, in some cases, the absence of an aggregate is actually the criterion enabling the detection of an
     * anachronism (e.g. two events issued in sequence by the same source, event one causing the creation of an
     * aggregate, event two updating the created aggregate and event two being handled first because event one
     * is handled by a slower processor).
     *
     * @param message The message
     * @param id The main or secondary identifier of the aggregate
     * @param aggregate The aggregate (if it exists) telling if the message is anachronical or not.
     *
     * @throws SameOperationException If the message was already handled
     * @throws RetryOperationException If another message should be handled first
     */
    default void validChronologyOrElseThrow(M message, K id, Optional<A> aggregate) {
        // Optional check
    }

    /**
     * An Aggregate has a main identifier which is the identifier of the AggregateRoot (K in the type declaration
     * of AggregateRoot<K, D>). The main identifier is the one
     * used by default when fetching an Aggregate using a Repository (see get(K) and getOptional(K) methods).
     *
     * Most of the time, runners are able to directly extract the main identifier out of a message so that aggregates
     * to update are identified by their main identifier. However, there are situations where the aggregate to update
     * cannot be identified using its main identifier. A "secondary" identifier is then used.
     *
     * To enable this, the second type argument K of a runner may be different from the main identifier of the
     * aggregate. It is then necessary to tell the Runtime how to fetch aggregates given the secondary identifier.
     * This is the purpose of SecondaryIdentifierHandler class: provide an aggregate given its alternative
     * identifier and extract the secondary identifier out of an existing aggregate (necessary in the case of expected
     * creations, also identified using the secondary identifier).
     *
     * @return A SecondaryIdentifierHandler instance or null if none is needed (i.e. the main identifier is used).
     */
    default SecondaryIdentifierHandler<K, A> secondaryIdentifierHandler() {
        return null;
    }
}
