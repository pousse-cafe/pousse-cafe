package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.context.TestDomainEvent3;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;

@Aggregate(
  factory = SimpleAggregateFactory.class,
  repository = SimpleAggregateRepository.class
)
public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Attributes> {

    @MessageListener(runner = SimpleAggregateTouchRunner.class)
    public void touch(TestDomainEvent3 event) {
        attributes().data().value("touched");
    }

    public static interface Attributes extends EntityAttributes<SimpleAggregateKey> {

        Attribute<String> data();
    }

}
