package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregate.Data;
import poussecafe.domain.SimpleAggregateFactory;
import poussecafe.domain.SimpleAggregateKey;
import poussecafe.domain.SimpleAggregateRepository;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

public class SimpleAggregateConfiguration extends
ActiveStorableConfiguration<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data, SimpleAggregateFactory, SimpleAggregateRepository> {

    public SimpleAggregateConfiguration() {
        super(SimpleAggregate.class, SimpleAggregateFactory.class, SimpleAggregateRepository.class);
    }

    @Override
    protected StorableDataFactory<Data> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(SimpleAggregate.Data.class);
    }

    @Override
    protected StorableDataAccess<SimpleAggregateKey, Data> dataAccess() {
        return new InMemoryDataAccess<>(SimpleAggregate.Data.class);
    }

}
