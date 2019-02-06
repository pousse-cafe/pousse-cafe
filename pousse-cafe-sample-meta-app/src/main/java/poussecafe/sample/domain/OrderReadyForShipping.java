package poussecafe.sample.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderReadyForShipping extends DomainEvent {

    Attribute<OrderKey> orderKey();
}
