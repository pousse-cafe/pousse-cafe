package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;
import poussecafe.property.Property;

public interface MessageCreated extends DomainEvent {

    Property<MessageKey> messageKey();
}
