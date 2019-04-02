package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderCreated extends DomainEvent {

    Attribute<OrderId> orderId();
}
