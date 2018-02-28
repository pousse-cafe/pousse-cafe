package poussecafe.domain;

import poussecafe.storable.ActiveStorableData;

public abstract class AggregateRoot<K, D extends ActiveStorableData<K>> extends Entity<K, D> {

}
