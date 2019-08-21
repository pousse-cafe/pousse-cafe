package poussecafe.mymodule.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

public interface AnotherDomainEvent extends DomainEvent {

    Attribute<MyAggregateId> identifier();

    Attribute<Integer> x();
}
