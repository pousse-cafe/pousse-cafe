package poussecafe.listeners;

import java.util.Objects;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.Environment;
import poussecafe.messaging.Message;

@SuppressWarnings({"rawtypes"})
public abstract class AggregateStateAwareRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    public AggregateStateAwareRunner(Class<A> aggregateRootClass) {
        Objects.requireNonNull(aggregateRootClass);
        this.aggregateRootClass = aggregateRootClass;
    }

    private Class<A> aggregateRootClass;

    private Environment environment;

    protected Repository aggregateRepository() {
        if(repository == null) {
            repository = environment.repositoryOf(aggregateRootClass).orElseThrow();
        }
        return repository;
    }

    private Repository repository;
}
