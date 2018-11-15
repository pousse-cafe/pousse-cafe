package poussecafe.simplemetaapp.domain;
import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

/*
 * This Domain Event is emitted by a MyAggregate instance.
 */
public interface MyDomainEvent extends DomainEvent {

    Property<MyAggregateKey> key();
}
