package poussecafe.environment;

import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.TestDomainEvent3;

public class MultiLevelTypesRunner
extends BaseMultiLevelTypesRunner<TestDomainEvent3> {

    @Override
    protected SimpleAggregateId aggregateId(TestDomainEvent3 message) {
        return message.identifier().value();
    }
}
