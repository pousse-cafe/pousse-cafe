package poussecafe.configuration;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;
import poussecafe.storage.ConsequenceEmissionPolicy;

public class ActiveStorableConfiguration<K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>>
extends StorableConfiguration<K, A, D, F, R> {

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    public ActiveStorableConfiguration(Class<A> storableClass, Class<D> dataClass,
            StorableServiceFactory<F, R> serviceFactory) {
        super(storableClass, dataClass, serviceFactory);
    }

    public ActiveStorableConfiguration(Class<A> storableClass, Class<D> dataClass, Class<F> factoryClass,
            Class<R> repositoryClass) {
        super(storableClass, dataClass, factoryClass, repositoryClass);
    }

    @Override
    public void setStorageServices(StorageServices<K, D> storageServices) {
        super.setStorageServices(storageServices);
        this.consequenceEmissionPolicy = storageServices.getConsequenceEmissionPolicy();
    }

    @Override
    public Singleton<R> getConfiguredRepository() {
        Singleton<R> repository = super.getConfiguredRepository();
        repository.get().setConsequenceEmissionPolicy(consequenceEmissionPolicy);
        return repository;
    }

}
