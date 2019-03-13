package poussecafe.myboundedcontext.domain.myaggregate;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.myboundedcontext.domain.AnotherDomainEvent;

import static poussecafe.collection.Collections.asSet;

public class DoSomethingElseRunner extends DefaultAggregateMessageListenerRunner<AnotherDomainEvent, MyAggregateKey, MyAggregate> {

    @Override
    public Set<MyAggregateKey> targetAggregatesKeys(AnotherDomainEvent message) {
        return asSet(message.key().value());
    }
}
