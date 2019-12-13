package poussecafe.mymodule.domain.myaggregate;

import poussecafe.listeners.AlwaysUpdateOneRunner;
import poussecafe.mymodule.domain.AnotherDomainEvent;

public class DoSomethingElseRunner
extends AlwaysUpdateOneRunner<AnotherDomainEvent, MyAggregateId, MyAggregate> {

    @Override
    protected MyAggregateId aggregateId(AnotherDomainEvent message) {
        return message.identifier().value();
    }
}
