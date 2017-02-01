package poussecafe.domain;

import poussecafe.domain.SimpleAggregate.Data;
import poussecafe.storable.ActiveStorableRepositoryTest;

import static org.mockito.Mockito.mock;

public class SimpleRepositoryTest
extends ActiveStorableRepositoryTest<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data> {

    @Override
    protected Repository<SimpleAggregate, SimpleAggregateKey, SimpleAggregate.Data> buildRepository() {
        return new SimpleAggregateRepository();
    }

    @Override
    protected SimpleAggregateKey buildKey() {
        return new SimpleAggregateKey();
    }

    @Override
    protected Class<Data> dataClass() {
        return SimpleAggregate.Data.class;
    }

    @Override
    protected SimpleAggregate mockStorable() {
        return mock(SimpleAggregate.class);
    }

}
