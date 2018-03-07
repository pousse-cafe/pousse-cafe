package poussecafe.domain;

import poussecafe.storable.IdentifiedStorableData;

public abstract class AggregateRoot<K, D extends IdentifiedStorableData<K>> extends Entity<K, D> {

}
