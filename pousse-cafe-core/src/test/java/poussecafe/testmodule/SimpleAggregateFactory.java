package poussecafe.testmodule;

import java.util.List;
import java.util.Optional;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;

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

    @MessageListener
    public Optional<SimpleAggregate> newSimpleAggregate(TestDomainEvent3 event) {
        throw new IllegalArgumentException(); // reproduce bug https://github.com/pousse-cafe/pousse-cafe/issues/130
    }
}
