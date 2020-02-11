package poussecafe.mymodule.domain.myaggregate;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.mymodule.ACommand;

public class DoSomethingRunner
extends UpdateOneRunner<ACommand, MyAggregateId, MyAggregate> {

    @Override
    protected MyAggregateId aggregateId(ACommand message) {
        return message.id().value();
    }
}
