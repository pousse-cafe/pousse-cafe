package poussecafe.discovery;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.testmodule.TestDomainEvent;
import poussecafe.testmodule.TestDomainEvent2;
import poussecafe.testmodule.TestDomainEvent3;

public class AggregateRootWithSingleListener extends AggregateRoot<String, EntityAttributes<String>> {

    @Override
    @ProducesEvent(TestDomainEvent3.class)
    public void onAdd() {
        issue(new TestDomainEvent3());
    }

    @MessageListener
    @ProducesEvent(value = TestDomainEvent2.class, required = false)
    @ProducesEvent(TestDomainEvent3.class)
    public void listener(TestDomainEvent event) {
        issue(new TestDomainEvent3());
    }
}
