package poussecafe.myboundedcontext.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

public interface YetAnotherDomainEvent extends DomainEvent {

    Attribute<MyAggregateKey> key();
}
