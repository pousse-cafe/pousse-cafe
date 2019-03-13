package poussecafe.domain;

import java.util.Set;
import poussecafe.context.TestDomainEvent3;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;

import static poussecafe.collection.Collections.asSet;

public class SimpleAggregateTouchRunner extends DefaultAggregateMessageListenerRunner<TestDomainEvent3, SimpleAggregateKey, SimpleAggregate> {

    @Override
    public Set<SimpleAggregateKey> targetAggregatesKeys(TestDomainEvent3 event) {
        return asSet(event.key().value());
    }
}
