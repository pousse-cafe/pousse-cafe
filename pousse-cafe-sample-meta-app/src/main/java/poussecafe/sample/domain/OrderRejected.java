package poussecafe.sample.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderRejected extends DomainEvent {

    Attribute<ProductKey> productKey();

    Attribute<OrderDescription> description();
}
