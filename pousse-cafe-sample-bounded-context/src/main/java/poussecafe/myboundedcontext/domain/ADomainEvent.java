package poussecafe.myboundedcontext.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;

public interface ADomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();
}
