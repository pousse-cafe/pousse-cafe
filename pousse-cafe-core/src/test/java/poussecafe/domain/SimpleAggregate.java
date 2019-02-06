package poussecafe.domain;

import poussecafe.contextconfigurer.Aggregate;

@Aggregate(
  factory = SimpleAggregateFactory.class,
  repository = SimpleAggregateRepository.class
)
public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Attributes> {

    public static interface Attributes extends EntityAttributes<SimpleAggregateKey> {

    }

}
