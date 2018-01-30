package poussecafe.configuration;

import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorableServices {

    private Class<?> storableClass;

    private IdentifiedStorableRepository<?, ?, ?> repository;

    private IdentifiedStorableFactory<?, ?, ?> factory;

    public StorableServices(Class<?> storableClass, IdentifiedStorableRepository<?, ?, ?> repository,
            IdentifiedStorableFactory<?, ?, ?> factory) {
        setStorableClass(storableClass);
        setRepository(repository);
        setFactory(factory);
    }

    public Class<?> getStorableClass() {
        return storableClass;
    }

    private void setStorableClass(Class<?> storableClass) {
        checkThat(value(storableClass).notNull().because("Storable class cannot be null"));
        this.storableClass = storableClass;
    }

    public IdentifiedStorableRepository<?, ?, ?> getRepository() {
        return repository;
    }

    private void setRepository(IdentifiedStorableRepository<?, ?, ?> repository) {
        checkThat(value(repository).notNull().because("Repository cannot be null"));
        this.repository = repository;
    }

    public IdentifiedStorableFactory<?, ?, ?> getFactory() {
        return factory;
    }

    private void setFactory(IdentifiedStorableFactory<?, ?, ?> factory) {
        checkThat(value(factory).notNull().because("Factory cannot be null"));
        this.factory = factory;
    }

}
