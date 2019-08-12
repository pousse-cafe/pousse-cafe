package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.runtime.TestDomainEvent3;

@Aggregate(
  factory = SimpleAggregateFactory.class,
  repository = SimpleAggregateRepository.class
)
public class SimpleAggregate extends AggregateRoot<SimpleAggregateId, SimpleAggregate.Attributes> {

    @MessageListener(runner = SimpleAggregateTouchRunner.class)
    public void touch(TestDomainEvent3 event) {
        attributes().data().value("touched");
    }

    public static interface Attributes extends EntityAttributes<SimpleAggregateId> {

        Attribute<String> data();
    }

}
