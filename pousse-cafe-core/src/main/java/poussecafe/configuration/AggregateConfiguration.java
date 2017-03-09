package poussecafe.configuration;

import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

public class AggregateConfiguration<K, A extends AggregateRoot<K, D>, D extends AggregateData<K>, F extends Factory<K, A, D>, R extends Repository<A, K, D>>
extends ActiveStorableConfiguration<K, A, D, F, R> {

    public AggregateConfiguration(Class<A> aggregateClass, Class<D> dataClass,
            StorableServiceFactory<F, R> serviceFactory) {
        super(aggregateClass, dataClass, serviceFactory);
    }

    public AggregateConfiguration(Class<A> aggregateClass, Class<D> dataClass, Class<F> factoryClass,
            Class<R> repositoryClass) {
        super(aggregateClass, dataClass, factoryClass, repositoryClass);
    }

}
