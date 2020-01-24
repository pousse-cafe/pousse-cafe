package poussecafe.discovery;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.TestDomainEvent;
import poussecafe.runtime.TestDomainEvent2;
import poussecafe.runtime.TestDomainEvent3;

public class AggregateRootWithSingleListener extends AggregateRoot<String, EntityAttributes<String>> {

    @MessageListener
    @ProducesEvent(event = TestDomainEvent2.class)
    @ProducesEvent(event = TestDomainEvent3.class, required = true)
    public void listener(TestDomainEvent event) {
        emitDomainEvent(new TestDomainEvent3());
    }
}
