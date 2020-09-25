package poussecafe.source.generation.generated;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(factory = MyAggregateFactory.class, repository = MyAggregateRepository.class)
public class MyAggregate extends AggregateRoot<MyAggregateId, MyAggregate.Attributes> {

    public static interface Attributes extends EntityAttributes<MyAggregateId> {
    }
}