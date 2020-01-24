package poussecafe.discovery;

import poussecafe.domain.EntityAttributes;
import poussecafe.domain.Repository;
import poussecafe.runtime.TestDomainEvent;
import poussecafe.runtime.TestDomainEvent2;
import poussecafe.runtime.TestDomainEvent3;

public class RepositoryWithSingleListener extends Repository<AggregateRootWithSingleListener, String, EntityAttributes<String>> {

    @MessageListener
    @ProducesEvent(value = TestDomainEvent2.class, required = false)
    @ProducesEvent(TestDomainEvent3.class)
    public void listener(TestDomainEvent event) {

    }
}
