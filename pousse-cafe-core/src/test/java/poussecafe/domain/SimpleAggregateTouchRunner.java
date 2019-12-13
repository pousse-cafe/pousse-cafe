package poussecafe.domain;

import poussecafe.listeners.AlwaysUpdateOneRunner;
import poussecafe.runtime.TestDomainEvent3;

public class SimpleAggregateTouchRunner
extends AlwaysUpdateOneRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
