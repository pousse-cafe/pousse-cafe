package poussecafe.configuration;

import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateFactory;
import poussecafe.domain.SimpleAggregateKey;
import poussecafe.domain.SimpleAggregateRepository;

public class SimpleAggregateConfiguration extends
ActiveStorableConfiguration<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Data, SimpleAggregateFactory, SimpleAggregateRepository> {

    public SimpleAggregateConfiguration() {
        super(SimpleAggregate.class, SimpleAggregate.Data.class, SimpleAggregateFactory.class,
                SimpleAggregateRepository.class);
    }

}
