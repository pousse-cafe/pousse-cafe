package poussecafe.environment;

import java.util.Optional;

@FunctionalInterface
public interface AggregateRetriever<K, A> {

    Optional<A> retrieve(K identifier);
}
