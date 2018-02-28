package poussecafe.domain;

import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableData;

public abstract class Entity<K, D extends ActiveStorableData<K>> extends ActiveStorable<K, D> implements DomainObject {

    protected void addDomainEvent(DomainEvent event) {
        getData().messageCollection().addMessage(event);
    }
}
