package poussecafe.domain;

import poussecafe.data.memory.InMemoryDataFactory;

public class SimpleAggregateFactory extends Factory<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data> {

    public SimpleAggregateFactory() {
        setStorableDataFactory(new InMemoryDataFactory<>(SimpleAggregate.Data.class));
    }

    @Override
    protected SimpleAggregate newAggregate() {
        return new SimpleAggregate();
    }
}
