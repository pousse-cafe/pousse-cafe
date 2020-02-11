package poussecafe.domain;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.runtime.TestDomainEvent3;

public class SimpleAggregateTouchRunner
extends UpdateOneRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
