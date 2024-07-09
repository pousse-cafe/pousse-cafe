package poussecafe.testmodule;

import poussecafe.listeners.UpdateOneRunner;

public class SimpleAggregateTouchAfterUpdateRunner extends UpdateOneRunner<TestDomainEvent6, SimpleAggregateId, SimpleAggregate> {

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent6 message) {
        return message.identifier().value();
    }

}
