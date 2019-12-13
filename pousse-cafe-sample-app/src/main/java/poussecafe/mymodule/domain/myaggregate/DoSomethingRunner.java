package poussecafe.mymodule.domain.myaggregate;

import poussecafe.listeners.AlwaysUpdateOneRunner;
import poussecafe.mymodule.ACommand;

public class DoSomethingRunner
extends AlwaysUpdateOneRunner<ACommand, MyAggregateId, MyAggregate> {

    @Override
    protected MyAggregateId aggregateId(ACommand message) {
        return message.id().value();
    }
}
