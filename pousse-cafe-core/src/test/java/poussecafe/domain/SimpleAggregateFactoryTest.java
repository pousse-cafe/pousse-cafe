package poussecafe.domain;

import poussecafe.storable.ActiveStorableFactoryTest;

public class SimpleAggregateFactoryTest
extends
ActiveStorableFactoryTest<SimpleAggregateKey, SimpleAggregate.Data, SimpleAggregate, SimpleAggregateFactory> {

    @Override
    protected SimpleAggregateKey buildKey() {
        return new SimpleAggregateKey();
    }

    @Override
    protected SimpleAggregateFactory factory() {
        return new SimpleAggregateFactory();
    }

}
