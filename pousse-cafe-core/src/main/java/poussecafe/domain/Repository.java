package poussecafe.domain;

import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends StorableData>
        extends ActiveStorableRepository<A, K, D> {

}
