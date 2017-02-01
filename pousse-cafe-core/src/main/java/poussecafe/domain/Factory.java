package poussecafe.domain;

import poussecafe.storable.ActiveStorableFactory;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends AggregateData<K>>
extends ActiveStorableFactory<K, A, D> {

    @Override
    protected A newStorable() {
        return newAggregate();
    }

    protected abstract A newAggregate();

    protected A newAggregateWithKey(K key) {
        return newStorableWithKey(key);
    }

}
