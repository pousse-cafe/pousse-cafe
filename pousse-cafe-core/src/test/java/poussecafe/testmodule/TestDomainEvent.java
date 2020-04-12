package poussecafe.testmodule;

import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@MessageImplementation(message = TestDomainEvent.class)
public class TestDomainEvent implements DomainEvent {

}
