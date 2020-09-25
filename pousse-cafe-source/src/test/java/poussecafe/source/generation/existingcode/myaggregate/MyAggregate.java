package poussecafe.source.generation.existingcode.myaggregate;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = MyAggregateFactory.class,
    repository = MyAggregateRepository.class
)
public class MyAggregate
extends AggregateRoot<MyAggregateId, MyAggregate.Attributes> {

    public static interface Attributes extends EntityAttributes<MyAggregateId> {

    }
}
