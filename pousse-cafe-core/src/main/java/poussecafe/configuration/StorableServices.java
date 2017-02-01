package poussecafe.configuration;

import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorableServices {

    private Class<?> storableClass;

    private ActiveStorableRepository<?, ?, ?> repository;

    private ActiveStorableFactory<?, ?, ?> factory;

    public StorableServices(Class<?> storableClass, ActiveStorableRepository<?, ?, ?> repository,
            ActiveStorableFactory<?, ?, ?> factory) {
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

    public ActiveStorableRepository<?, ?, ?> getRepository() {
        return repository;
    }

    private void setRepository(ActiveStorableRepository<?, ?, ?> repository) {
        checkThat(value(repository).notNull().because("Repository cannot be null"));
        this.repository = repository;
    }

    public ActiveStorableFactory<?, ?, ?> getFactory() {
        return factory;
    }

    private void setFactory(ActiveStorableFactory<?, ?, ?> factory) {
        checkThat(value(factory).notNull().because("Factory cannot be null"));
        this.factory = factory;
    }

}
