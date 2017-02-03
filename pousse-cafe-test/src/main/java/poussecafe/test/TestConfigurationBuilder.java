package poussecafe.test;

import poussecafe.configuration.ActiveStorableConfiguration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;

@SuppressWarnings("rawtypes")
public class TestConfigurationBuilder {

    private ActiveStorableConfiguration configuration;

    private Class dataClass;

    private StorableDataAccess dataAccess;

    public TestConfigurationBuilder withConfiguration(ActiveStorableConfiguration<?, ?, ?, ?, ?> configuration) {
        this.configuration = configuration;
        return this;
    }

    public TestConfigurationBuilder withData(Class<?> dataClass) {
        this.dataClass = dataClass;
        return this;
    }

    public TestConfigurationBuilder withDataAccess(StorableDataAccess dataAccess) {
        this.dataAccess = dataAccess;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>> ActiveStorableConfiguration<K, A, D, F, R> build() {
        if (dataAccess == null) {
            configuration.setDataAccess(new InMemoryDataAccess<>(dataClass));
        } else {
            configuration.setDataAccess(dataAccess);
        }
        configuration.setDataFactory(new InMemoryDataFactory<>(dataClass));
        return configuration;
    }

}
