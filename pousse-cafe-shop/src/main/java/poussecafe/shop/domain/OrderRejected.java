package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderRejected extends DomainEvent {

    Attribute<ProductId> productId();

    Attribute<OrderDescription> description();
}
