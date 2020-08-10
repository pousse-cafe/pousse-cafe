package poussecafe.testmodule2;

import poussecafe.listeners.UpdateOrCreateOneRunner;
import poussecafe.testmodule2.SimpleAggregate.SimpleAggregateRoot;

public class SimpleAggregateTouchRunner
extends UpdateOrCreateOneRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregateRoot> {

    public SimpleAggregateTouchRunner() {
        super(SimpleAggregateRoot.class);
    }

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
