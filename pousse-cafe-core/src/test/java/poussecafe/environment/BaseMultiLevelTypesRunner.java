package poussecafe.environment;

import poussecafe.listeners.UpdateOrCreateOneRunner;
import poussecafe.messaging.Message;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateId;

public abstract class BaseMultiLevelTypesRunner<M extends Message>
extends UpdateOrCreateOneRunner<M, SimpleAggregateId, SimpleAggregate> {

    public BaseMultiLevelTypesRunner() {
        super(SimpleAggregate.class);
    }
}
