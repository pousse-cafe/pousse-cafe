package poussecafe.discovery;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.TestDomainEvent;
import poussecafe.runtime.TestDomainEvent2;
import poussecafe.runtime.TestDomainEvent3;

public class AggregateRootWithSingleListener extends AggregateRoot<String, EntityAttributes<String>> {

    @Override
    @ProducesEvent(event = TestDomainEvent3.class)
    public void onAdd() {
        emitDomainEvent(new TestDomainEvent3());
    }

    @MessageListener
    @ProducesEvent(event = TestDomainEvent2.class, required = false)
    @ProducesEvent(event = TestDomainEvent3.class)
    public void listener(TestDomainEvent event) {
        emitDomainEvent(new TestDomainEvent3());
    }
}
