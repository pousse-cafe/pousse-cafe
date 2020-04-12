package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = SimpleAggregateFactory.class,
    repository = SimpleAggregateRepository.class,
    module = TestModule.class
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
