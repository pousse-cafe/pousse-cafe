package poussecafe.domain;

import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.IdentifiedStorableData;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends IdentifiedStorableData<K>>
        extends ActiveStorableRepository<A, K, D> {

}
