package poussecafe.configuration;

import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;

public interface StorableServiceFactory<F extends IdentifiedStorableFactory<?, ?, ?>, R extends IdentifiedStorableRepository<?, ?, ?>> {

    R newRepository();

    F newFactory();

}
