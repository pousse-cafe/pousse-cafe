package poussecafe.domain;

import poussecafe.data.memory.InMemoryStorableDataFactory;

public class SimpleAggregateFactory extends Factory<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data> {

    public SimpleAggregateFactory() {
        setStorableDataFactory(new InMemoryStorableDataFactory<>(SimpleAggregate.Data.class));
    }

    @Override
    protected SimpleAggregate newAggregate() {
        return new SimpleAggregate();
    }
}
