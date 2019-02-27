package poussecafe.myboundedcontext.domain;
import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

/*
 * This Domain Event is emitted by a MyAggregate instance.
 */
public interface MyDomainEvent extends DomainEvent {

    Attribute<MyAggregateKey> key();
}
