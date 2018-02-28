package poussecafe.domain;

import poussecafe.storable.ActiveStorableData;
import poussecafe.storable.ActiveStorableRepository;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends ActiveStorableData<K>>
        extends ActiveStorableRepository<A, K, D> {

}
