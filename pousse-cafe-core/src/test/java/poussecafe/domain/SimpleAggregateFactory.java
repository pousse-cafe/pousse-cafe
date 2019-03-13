package poussecafe.domain;

import java.util.List;
import poussecafe.context.TestDomainEvent;
import poussecafe.context.TestDomainEvent2;
import poussecafe.discovery.MessageListener;

import static java.util.Arrays.asList;

public class SimpleAggregateFactory extends Factory<SimpleAggregateKey, SimpleAggregate, SimpleAggregate.Attributes> {

    @MessageListener
    public SimpleAggregate newSimpleAggregate(TestDomainEvent event) {
        return newSimpleAggregate(new SimpleAggregateKey("id1"));
    }

    private SimpleAggregate newSimpleAggregate(SimpleAggregateKey key) {
        SimpleAggregate aggregate = newAggregateWithKey(key);
        aggregate.attributes().data().value("untouched");
        return aggregate;
    }

    @MessageListener
    public List<SimpleAggregate> newSimpleAggregate(TestDomainEvent2 event) {
        return asList(newSimpleAggregate(new SimpleAggregateKey("id1")),
                newSimpleAggregate(new SimpleAggregateKey("id2")));
    }
}
