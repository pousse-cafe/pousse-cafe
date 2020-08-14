package poussecafe.source.generation.generatedinternal;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(factory = MyAggregateFactory.class, repository = MyAggregateRepository.class, module = DefaultModule.class)
public class MyAggregate extends AggregateRoot<MyAggregateId, MyAggregate.Attributes> {

    public static interface Attributes extends EntityAttributes<MyAggregateId> {
    }
}