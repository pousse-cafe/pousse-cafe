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

    private Singleton<R> repository;

    private Singleton<F> factory;

    private StorableDataFactory<D> aggregateDataFactory;

    private StorableDataAccess<K, D> dataAccess;

    public StorableConfiguration(Class<A> storableClass, StorableServiceFactory<F, R> serviceFactory) {
        setStorableClass(storableClass);

        repository = new Singleton<>(serviceFactory::newRepository);
        factory = new Singleton<>(serviceFactory::newFactory);
    }

    public StorableConfiguration(Class<A> storableClass, Class<F> factoryClass, Class<R> repositoryClass) {
        this(storableClass, new ClassBasedStorableServiceFactory<>(factoryClass, repositoryClass));
    }

    private void setStorableClass(Class<A> storableClass) {
        checkThat(value(storableClass).notNull().because("Storable class cannot be null"));
        this.storableClass = storableClass;
    }

    public void setDataFactory(StorableDataFactory<D> aggregateDataFactory) {
        checkThat(value(aggregateDataFactory).notNull().because("Data factory cannot be null"));
        this.aggregateDataFactory = aggregateDataFactory;
    }

    public void setDataAccess(StorableDataAccess<K, D> dataAccess) {
        checkThat(value(dataAccess).notNull().because("Data access cannot be null"));
        this.dataAccess = dataAccess;
    }

    public Class<A> getStorableClass() {
        return storableClass;
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
