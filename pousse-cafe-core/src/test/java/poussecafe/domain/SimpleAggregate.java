package poussecafe.domain;

import poussecafe.attribute.Attribute;
import poussecafe.context.TestDomainEvent3;
import poussecafe.contextconfigurer.Aggregate;
import poussecafe.messaging.DomainEventListener;

@Aggregate(
  factory = SimpleAggregateFactory.class,
  repository = SimpleAggregateRepository.class
)
public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Attributes> {

    @DomainEventListener(runner = SimpleAggregateTouchRunner.class)
    public void touch(TestDomainEvent3 event) {
        attributes().data().value("touched");
    }

    public static interface Attributes extends EntityAttributes<SimpleAggregateKey> {

        Attribute<String> data();
    }

}
