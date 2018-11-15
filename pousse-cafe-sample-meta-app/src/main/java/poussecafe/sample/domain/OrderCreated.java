package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface OrderCreated extends DomainEvent {

    Property<OrderKey> orderKey();
}
