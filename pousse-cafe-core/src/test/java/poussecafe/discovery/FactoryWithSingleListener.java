package poussecafe.discovery;

import poussecafe.domain.EntityAttributes;
import poussecafe.domain.Factory;
import poussecafe.testmodule.TestDomainEvent;
import poussecafe.testmodule.TestDomainEvent2;

public class FactoryWithSingleListener extends Factory<String, AggregateRootWithSingleListener, EntityAttributes<String>> {

    @MessageListener
    @ProducesEvent(value = TestDomainEvent2.class, required = false)
    public AggregateRootWithSingleListener listener(TestDomainEvent event) {
        return new AggregateRootWithSingleListener();
    }
}
