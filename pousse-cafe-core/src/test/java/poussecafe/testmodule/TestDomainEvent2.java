package poussecafe.testmodule;

import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@MessageImplementation(message = TestDomainEvent2.class)
public class TestDomainEvent2 implements DomainEvent {

}
