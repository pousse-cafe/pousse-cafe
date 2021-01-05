package poussecafe.source.validation.message;

import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@MessageImplementation(message = AutoImplementedEvent.class)
public class AutoImplementedEvent implements DomainEvent {

}
