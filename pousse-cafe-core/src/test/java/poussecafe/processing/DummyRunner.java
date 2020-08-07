package poussecafe.processing;

import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.SimpleMessage;

public class DummyRunner implements AggregateMessageListenerRunner<SimpleMessage, SimpleAggregateId, SimpleAggregate> {

}
