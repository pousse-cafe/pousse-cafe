package poussecafe.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.runtime.TestDomainEvent4;

public class SimpleAggregateRepository extends Repository<SimpleAggregate, SimpleAggregateId, SimpleAggregate.Attributes> {

    @MessageListener
    public void delete(TestDomainEvent4 event) {
        delete(event.identifier().value());
    }
}
