package poussecafe.domain;

import poussecafe.storable.ActiveStorable;

public abstract class AggregateRoot<K, D extends AggregateData<K>> extends ActiveStorable<K, D> {

    protected void addDomainEvent(DomainEvent event) {
        getMessageCollection().addMessage(event);
    }
}
