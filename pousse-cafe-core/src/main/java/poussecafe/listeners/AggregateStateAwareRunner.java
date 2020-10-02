package poussecafe.listeners;

import java.util.Objects;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.Environment;
import poussecafe.environment.SecondaryIdentifierHandler;
import poussecafe.messaging.Message;

@SuppressWarnings({"rawtypes"})
public abstract class AggregateStateAwareRunner<M extends Message, K, A extends AggregateRoot<?, ?>>
implements AggregateMessageListenerRunner<M, K, A> {

    public AggregateStateAwareRunner(Class<A> aggregateRootClass) {
        Objects.requireNonNull(aggregateRootClass);
        this.aggregateRootClass = aggregateRootClass;
    }

    private Class<A> aggregateRootClass;

    private Environment environment;

    protected AggregateRepository aggregateRepository() {
        if(repository == null) {
            repository = environment.repositoryOf(aggregateRootClass).orElseThrow();
        }
        return repository;
    }

    private AggregateRepository repository;

    @SuppressWarnings({ "unchecked" })
    protected boolean existsById(K identifier) {
        SecondaryIdentifierHandler<K, A> secondaryIdentifierHandler = secondaryIdentifierHandler();
        if(secondaryIdentifierHandler == null) {
            return aggregateRepository().existsById(identifier);
        } else {
            return secondaryIdentifierHandler.aggregateRetriever().retrieve(identifier).isPresent();
        }
    }
}
