package poussecafe.sample.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface OrderPlaced extends DomainEvent {

    Attribute<ProductKey> productKey();

    Attribute<OrderDescription> description();
}
