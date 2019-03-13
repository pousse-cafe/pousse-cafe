package poussecafe.domain;

import poussecafe.context.TestDomainEvent4;
import poussecafe.discovery.MessageListener;

public class SimpleAggregateRepository extends Repository<SimpleAggregate, SimpleAggregateKey, SimpleAggregate.Attributes> {

    @MessageListener
    public void delete(TestDomainEvent4 event) {
        delete(event.key().value());
    }
}
