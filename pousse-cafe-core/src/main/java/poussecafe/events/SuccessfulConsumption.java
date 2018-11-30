package poussecafe.events;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface SuccessfulConsumption extends DomainEvent {

    Property<String> consumptionId();

    Property<String> listenerId();

    Property<String> rawMessage();
}
