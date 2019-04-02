package poussecafe.myboundedcontext.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;

public interface YetAnotherDomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();
}
