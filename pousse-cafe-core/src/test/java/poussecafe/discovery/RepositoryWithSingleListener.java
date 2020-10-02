package poussecafe.discovery;

import poussecafe.domain.AggregateRepository;
import poussecafe.domain.EntityAttributes;
import poussecafe.testmodule.TestDomainEvent;
import poussecafe.testmodule.TestDomainEvent2;
import poussecafe.testmodule.TestDomainEvent3;

public class RepositoryWithSingleListener
extends AggregateRepository<AggregateRootWithSingleListener, String, EntityAttributes<String>> {

    @MessageListener
    @ProducesEvent(value = TestDomainEvent2.class, required = false)
    @ProducesEvent(TestDomainEvent3.class)
    public void listener(TestDomainEvent event) {

    }
}
