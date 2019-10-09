package poussecafe.mymodule.domain.myaggregate;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.mymodule.domain.AnotherDomainEvent;

import static poussecafe.collection.Collections.asSet;

public class DoSomethingElseRunner extends DefaultAggregateMessageListenerRunner<AnotherDomainEvent, MyAggregateId, MyAggregate> {

    @Override
    public Set<MyAggregateId> targetAggregatesIds(AnotherDomainEvent message) {
        return asSet(message.identifier().value());
    }
}
