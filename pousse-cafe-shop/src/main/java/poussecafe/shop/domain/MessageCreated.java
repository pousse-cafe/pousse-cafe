package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.domain.DomainEvent;

public interface MessageCreated extends DomainEvent {

    Attribute<MessageKey> messageKey();
}
