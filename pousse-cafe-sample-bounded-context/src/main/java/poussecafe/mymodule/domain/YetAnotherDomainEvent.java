package poussecafe.mymodule.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

public interface YetAnotherDomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();
}
