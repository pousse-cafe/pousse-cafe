package poussecafe.environment;

import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.TestDomainEvent3;

public class SingleLevelTypesImplementingRunner
implements AggregateMessageListenerRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

}
