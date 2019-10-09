package poussecafe.mymodule.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

public interface ADomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();
}
