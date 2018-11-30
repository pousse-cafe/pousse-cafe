package poussecafe.events;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface FailedConsumption extends DomainEvent {

    Property<String> consumptionId();

    Property<String> listenerId();

    Property<String> rawMessage();

    Property<String> error();
}
