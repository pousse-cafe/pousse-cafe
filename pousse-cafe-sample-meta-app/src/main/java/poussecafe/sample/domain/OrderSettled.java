package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface OrderSettled extends DomainEvent {

    Property<OrderKey> orderKey();
}
