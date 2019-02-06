package poussecafe.support.model;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface SuccessfulConsumption extends DomainEvent {

    Attribute<String> consumptionId();

    Attribute<String> listenerId();

    Attribute<String> rawMessage();
}
