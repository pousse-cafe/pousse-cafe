package poussecafe.listeners;

import java.util.Collection;
import poussecafe.domain.AggregateRoot;
import poussecafe.messaging.Message;

import static java.util.Arrays.asList;

public abstract class AlwaysUpdateOneRunner<M extends Message, K, A extends AggregateRoot<K, ?>>
extends AlwaysUpdateRunner<M, K, A> {

    @Override
    public Collection<K> aggregateIds(M message) {
        return asList(aggregateId(message));
    }

    protected abstract K aggregateId(M message);
}
