package poussecafe.source.generation.generatedinternal;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(factory = MyAggregateFactory.class, repository = MyAggregateRepository.class)
public class MyAggregateRoot extends AggregateRoot<MyAggregateId, MyAggregateRoot.Attributes> {

    public static interface Attributes extends EntityAttributes<MyAggregateId> {
    }
}