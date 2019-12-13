package poussecafe.listeners;

import java.util.Collection;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.Message;

import static java.util.Arrays.asList;

public abstract class UpdateOrCreateOneRunner <M extends Message, K, A extends AggregateRoot<K, ?>>
extends UpdateOrCreateRunner<M, K, A> {

    public UpdateOrCreateOneRunner(Class<A> aggregateRootClass) {
        super(aggregateRootClass);
    }

    @Override
    public Collection<K> aggregateIds(M message) {
        return asList(aggregateId(message));
    }

    protected abstract K aggregateId(M message);
}
