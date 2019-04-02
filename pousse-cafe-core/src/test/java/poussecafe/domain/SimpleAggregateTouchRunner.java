package poussecafe.domain;

import java.util.Set;
import poussecafe.context.TestDomainEvent3;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;

import static poussecafe.collection.Collections.asSet;

public class SimpleAggregateTouchRunner extends DefaultAggregateMessageListenerRunner<TestDomainEvent3, SimpleAggregateId, SimpleAggregate> {

    @Override
    public Set<SimpleAggregateId> targetAggregatesIds(TestDomainEvent3 event) {
        return asSet(event.identifier().value());
    }
}
