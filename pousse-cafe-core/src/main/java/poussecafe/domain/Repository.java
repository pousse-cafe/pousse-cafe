package poussecafe.domain;

import poussecafe.storable.ActiveStorableRepository;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends AggregateData<K>>
extends ActiveStorableRepository<A, K, D> {

    @Override
    protected A newStorable() {
        return newAggregate();
    }

    protected abstract A newAggregate();

}
