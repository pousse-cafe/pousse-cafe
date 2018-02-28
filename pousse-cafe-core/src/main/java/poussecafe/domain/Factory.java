package poussecafe.domain;

import poussecafe.storable.ActiveStorableData;
import poussecafe.storable.ActiveStorableFactory;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends ActiveStorableData<K>>
        extends ActiveStorableFactory<K, A, D> {

    protected A newAggregateWithKey(K key) {
        return newStorableWithKey(key);
    }

}
