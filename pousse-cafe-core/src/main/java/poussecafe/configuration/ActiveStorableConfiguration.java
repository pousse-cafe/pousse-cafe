package poussecafe.configuration;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;
import poussecafe.storage.ConsequenceEmissionPolicy;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ActiveStorableConfiguration<K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>>
extends StorableConfiguration<K, A, D, F, R> {

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    public ActiveStorableConfiguration(Class<A> storableClass, StorableServiceFactory<F, R> serviceFactory) {
        super(storableClass, serviceFactory);
    }

    public ActiveStorableConfiguration(Class<A> storableClass, Class<F> factoryClass, Class<R> repositoryClass) {
        super(storableClass, factoryClass, repositoryClass);
    }

    public void setConsequenceEmissionPolicy(ConsequenceEmissionPolicy consequenceEmissionPolicy) {
        checkThat(value(consequenceEmissionPolicy).notNull().because("Consequence emission policy cannot be null"));
        this.consequenceEmissionPolicy = consequenceEmissionPolicy;
    }

    @Override
    public Singleton<R> getConfiguredRepository() {
        Singleton<R> repository = super.getConfiguredRepository();
        repository.get().setConsequenceEmissionPolicy(consequenceEmissionPolicy);
        return repository;
    }

}
