package poussecafe.testmodule2;

import poussecafe.listeners.UpdateOrCreateOneRunner;
import poussecafe.testmodule2.SimpleAggregate.Root;

public class SimpleAggregateTouchRunner
extends UpdateOrCreateOneRunner<TestDomainEvent3, SimpleAggregateId, Root> {

    public SimpleAggregateTouchRunner() {
        super(Root.class);
    }

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
