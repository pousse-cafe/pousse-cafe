package poussecafe.sample.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderCreated extends DomainEvent {

    Attribute<OrderKey> orderKey();
}
