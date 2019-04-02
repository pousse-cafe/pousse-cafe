package poussecafe.myboundedcontext.domain;
import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;

/*
 * This Domain Event is emitted by a MyAggregate instance.
 */
public interface MyDomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();
}
