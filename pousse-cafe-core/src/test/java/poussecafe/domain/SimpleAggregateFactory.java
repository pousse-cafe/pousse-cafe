package poussecafe.domain;

import java.util.List;
import poussecafe.discovery.MessageListener;
import poussecafe.runtime.TestDomainEvent;
import poussecafe.runtime.TestDomainEvent2;

import static java.util.Arrays.asList;

public class SimpleAggregateFactory extends Factory<SimpleAggregateId, SimpleAggregate, SimpleAggregate.Attributes> {

    @MessageListener
    public SimpleAggregate newSimpleAggregate(TestDomainEvent event) {
        return newSimpleAggregate(new SimpleAggregateId("id1"));
    }

    private SimpleAggregate newSimpleAggregate(SimpleAggregateId id) {
        SimpleAggregate aggregate = newAggregateWithId(id);
        aggregate.attributes().data().value("untouched");
        return aggregate;
    }

    @MessageListener
    public List<SimpleAggregate> newSimpleAggregate(TestDomainEvent2 event) {
        return asList(newSimpleAggregate(new SimpleAggregateId("id1")),
                newSimpleAggregate(new SimpleAggregateId("id2")));
    }
}
