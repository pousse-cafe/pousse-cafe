package poussecafe.domain;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.StorableData;

public abstract class Entity<K, D extends StorableData> extends ActiveStorable<K, D> implements DomainObject {

    protected void addDomainEvent(DomainEvent event) {
        getMessageCollection().addMessage(event);
    }
}
