package poussecafe.testmodule;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Repository;

public class SimpleAggregateRepository extends Repository<SimpleAggregate, SimpleAggregateId, SimpleAggregate.Attributes> {

    @MessageListener
    public void delete(TestDomainEvent4 event) {
        delete(event.identifier().value());
    }
}
