package poussecafe.mymodule.domain.myaggregate;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.mymodule.domain.AnotherDomainEvent;

public class DoSomethingElseRunner
extends UpdateOneRunner<AnotherDomainEvent, MyAggregateId, MyAggregate> {

    @Override
    protected MyAggregateId aggregateId(AnotherDomainEvent message) {
        return message.identifier().value();
    }
}
