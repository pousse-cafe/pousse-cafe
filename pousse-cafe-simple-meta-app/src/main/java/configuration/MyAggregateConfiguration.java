package configuration;

import domain.MyAggregate;
import domain.MyAggregateKey;
import domain.MyFactory;
import domain.MyRepository;
import poussecafe.configuration.AggregateConfiguration;

public class MyAggregateConfiguration
extends AggregateConfiguration<MyAggregateKey, MyAggregate, MyAggregate.Data, MyFactory, MyRepository> {

    public MyAggregateConfiguration() {
        super(MyAggregate.class, MyFactory.class, MyRepository.class);
    }

}
