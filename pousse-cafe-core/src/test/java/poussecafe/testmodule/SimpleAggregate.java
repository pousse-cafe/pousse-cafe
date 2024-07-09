package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = SimpleAggregateFactory.class,
    repository = SimpleAggregateRepository.class
)
public class SimpleAggregate extends AggregateRoot<SimpleAggregateId, SimpleAggregate.Attributes> {

    @MessageListener(runner = SimpleAggregateTouchRunner.class)
    public void touch(TestDomainEvent3 event) {
        touch();
    }

    private void touch() {
        attributes().data().value("touched");
    }

    @MessageListener(runner = SimpleAggregateTouchByDataRunner.class)
    public void touch(TestDomainEvent5 event) {
        touch();
    }

    @MessageListener(runner = SimpleAggregateUpdateRunner.class)
    @ProducesEvent(value = TestDomainEvent6.class, required = true)
    public void update(UpdateSimpleAggregate command) {
        attributes().data().value("updated");

        var event = newDomainEvent(TestDomainEvent6.class);
        event.identifier().valueOf(attributes().identifier());
        issue(event);
    }

    @MessageListener(runner = SimpleAggregateTouchAfterUpdateRunner.class)
    public void touchAfterUpdate(TestDomainEvent6 event) {
        touch();
    }

    public static interface Attributes extends EntityAttributes<SimpleAggregateId> {

        Attribute<String> data();
    }
}
