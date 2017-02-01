package poussecafe.test;

import poussecafe.configuration.ActiveStorableConfiguration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class TestConfiguration<K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>>
extends ActiveStorableConfiguration<K, A, D, F, R> {

    private Class<D> dataClass;

    public TestConfiguration(Class<A> storableClass, Class<F> factoryClass, Class<R> repositoryClass,
            Class<D> dataClass) {
        super(storableClass, factoryClass, repositoryClass);
        setDataClass(dataClass);
    }

    private void setDataClass(Class<D> dataClass) {
        checkThat(value(dataClass).notNull().because("Data class cannot be null"));
        this.dataClass = dataClass;
    }

    @Override
    protected StorableDataFactory<D> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(dataClass);
    }

    @Override
    protected StorableDataAccess<K, D> dataAccess() {
        return new InMemoryDataAccess<>(dataClass);
    }

}
