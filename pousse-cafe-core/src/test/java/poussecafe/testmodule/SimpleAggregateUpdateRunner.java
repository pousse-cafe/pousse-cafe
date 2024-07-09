package poussecafe.testmodule;

import poussecafe.listeners.UpdateOneRunner;

public class SimpleAggregateUpdateRunner extends UpdateOneRunner<UpdateSimpleAggregate, SimpleAggregateId, SimpleAggregate> {

    @Override
    protected SimpleAggregateId aggregateId(UpdateSimpleAggregate message) {
        return message.identifier().value();
    }

}
