package poussecafe.domain;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.IdentifiedStorableData;

public abstract class Entity<K, D extends IdentifiedStorableData<K>> extends ActiveStorable<K, D> implements DomainObject {

    protected void addDomainEvent(DomainEvent event) {
        getMessageCollection().addMessage(event);
    }
}
