package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface OrderRejected extends DomainEvent {

    Property<ProductKey> productKey();

    Property<OrderDescription> description();
}
