package poussecafe.domain;

import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.StorableData;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends StorableData>
        extends ActiveStorableFactory<K, A, D> {

    protected A newAggregateWithKey(K key) {
        return newStorableWithKey(key);
    }

}
