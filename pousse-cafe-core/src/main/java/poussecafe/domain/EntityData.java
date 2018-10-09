package poussecafe.domain;

import poussecafe.property.Property;

public interface EntityData<K> {

    Property<K> key();
}
