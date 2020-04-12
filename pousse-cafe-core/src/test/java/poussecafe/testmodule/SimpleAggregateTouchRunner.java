package poussecafe.testmodule;

import poussecafe.listeners.UpdateOrCreateOneRunner;

public class SimpleAggregateTouchRunner
extends UpdateOrCreateOneRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

    public SimpleAggregateTouchRunner() {
        super(SimpleAggregate.class);
    }

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
