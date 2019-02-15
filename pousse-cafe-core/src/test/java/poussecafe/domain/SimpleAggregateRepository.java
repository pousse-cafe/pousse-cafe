package poussecafe.domain;

import poussecafe.context.TestDomainEvent4;
import poussecafe.messaging.DomainEventListener;

public class SimpleAggregateRepository extends Repository<SimpleAggregate, SimpleAggregateKey, SimpleAggregate.Attributes> {

    @DomainEventListener
    public void delete(TestDomainEvent4 event) {
        delete(event.key().value());
    }
}
