package poussecafe.sample.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderSettled extends DomainEvent {

    Attribute<OrderKey> orderKey();
}
