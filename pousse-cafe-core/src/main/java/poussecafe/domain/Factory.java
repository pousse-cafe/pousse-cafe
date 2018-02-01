package poussecafe.domain;

import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.IdentifiedStorableData;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends IdentifiedStorableData<K>>
        extends ActiveStorableFactory<K, A, D> {

    protected A newAggregateWithKey(K key) {
        return newStorableWithKey(key);
    }

}
