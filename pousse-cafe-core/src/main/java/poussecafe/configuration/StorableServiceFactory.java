package poussecafe.configuration;

import poussecafe.storable.StorableFactory;
import poussecafe.storable.StorableRepository;

public interface StorableServiceFactory<F extends StorableFactory<?, ?, ?>, R extends StorableRepository<?, ?, ?>> {

    R newRepository();

    F newFactory();

}
