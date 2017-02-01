package poussecafe.test;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;

public class TestConfigurationBuilder {

    private Class<?> storableClass;

    private Class<?> factoryClass;

    private Class<?> repositoryClass;

    private Class<?> dataClass;

    public TestConfigurationBuilder withStorable(Class<?> storableClass) {
        this.storableClass = storableClass;
        return this;
    }

    public TestConfigurationBuilder withFactory(Class<?> factoryClass) {
        this.factoryClass = factoryClass;
        return this;
    }

    public TestConfigurationBuilder withRepository(Class<?> repositoryClass) {
        this.repositoryClass = repositoryClass;
        return this;
    }

    public TestConfigurationBuilder withData(Class<?> dataClass) {
        this.dataClass = dataClass;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>> TestConfiguration<K, A, D, F, R> build() {
        return new TestConfiguration<>((Class<A>) storableClass, (Class<F>) factoryClass,
                (Class<R>) repositoryClass, (Class<D>) dataClass);
    }

}
