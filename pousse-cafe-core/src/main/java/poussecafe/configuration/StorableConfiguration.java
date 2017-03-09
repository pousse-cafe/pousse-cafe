package poussecafe.configuration;

import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;
import poussecafe.storable.StorableFactory;
import poussecafe.storable.StorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorableConfiguration<K, A extends Storable<K, D>, D extends StorableData<K>, F extends StorableFactory<K, A, D>, R extends StorableRepository<A, K, D>> {

    private Class<A> storableClass;

    private Class<D> dataClass;

    private Singleton<R> repository;

    private Singleton<F> factory;

    private StorableDataFactory<D> aggregateDataFactory;

    private StorableDataAccess<K, D> dataAccess;

    public StorableConfiguration(Class<A> storableClass, Class<D> dataClass,
            StorableServiceFactory<F, R> serviceFactory) {
        setStorableClass(storableClass);
        setDataClass(dataClass);

        repository = new Singleton<>(serviceFactory::newRepository);
        factory = new Singleton<>(serviceFactory::newFactory);
    }

    public StorableConfiguration(Class<A> storableClass, Class<D> dataClass, Class<F> factoryClass,
            Class<R> repositoryClass) {
        this(storableClass, dataClass, new ClassBasedStorableServiceFactory<>(factoryClass, repositoryClass));
    }

    private void setStorableClass(Class<A> storableClass) {
        checkThat(value(storableClass).notNull().because("Storable class cannot be null"));
        this.storableClass = storableClass;
    }

    private void setDataClass(Class<D> dataClass) {
        checkThat(value(dataClass).notNull().because("Data class cannot be null"));
        this.dataClass = dataClass;
    }

    public void setStorageServices(StorageServices<K, D> storageServices) {
        checkThat(value(storageServices).notNull().because("Storage services cannot be null"));
        this.aggregateDataFactory = storageServices.getDataFactory();
        this.dataAccess = storageServices.getDataAccess();
    }

    public Class<A> getStorableClass() {
        return storableClass;
    }

    public Class<D> getDataClass() {
        return dataClass;
    }

    public Singleton<F> getConfiguredFactory() {
        factory().get().setStorableDataFactory(aggregateDataFactory);
        return factory();
    }

    public Singleton<F> factory() {
        return factory;
    }

    public Singleton<R> getConfiguredRepository() {
        repository().get().setStorableDataFactory(aggregateDataFactory);
        repository().get().setDataAccess(dataAccess);
        return repository;
    }

    public Singleton<R> repository() {
        return repository;
    }

}
