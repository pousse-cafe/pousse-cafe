package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateFactory;
import poussecafe.domain.SimpleAggregateKey;
import poussecafe.domain.SimpleAggregateRepository;

public class SimpleAggregateConfiguration extends
ActiveStorableConfiguration<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data, SimpleAggregateFactory, SimpleAggregateRepository> {

    public SimpleAggregateConfiguration() {
        super(SimpleAggregate.class, SimpleAggregateFactory.class, SimpleAggregateRepository.class);
        setDataFactory(new InMemoryDataFactory<>(SimpleAggregate.Data.class));
        setDataAccess(new InMemoryDataAccess<>(SimpleAggregate.Data.class));
    }

}
