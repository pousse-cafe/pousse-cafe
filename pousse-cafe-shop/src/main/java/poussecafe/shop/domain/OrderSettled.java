package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderSettled extends DomainEvent {

    Attribute<OrderId> orderId();
}
