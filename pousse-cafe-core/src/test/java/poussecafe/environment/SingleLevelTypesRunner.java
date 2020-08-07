package poussecafe.environment;

import poussecafe.listeners.UpdateOrCreateOneRunner;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.TestDomainEvent3;

public class SingleLevelTypesRunner
extends UpdateOrCreateOneRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

    public SingleLevelTypesRunner() {
        super(SimpleAggregate.class);
    }

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
