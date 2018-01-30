package poussecafe.domain;

import poussecafe.storable.StorableData;

public abstract class AggregateRoot<K, D extends StorableData> extends Entity<K, D> {

}
