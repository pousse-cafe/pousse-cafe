package poussecafe.attribute.entity;

import java.util.Collection;
import poussecafe.attribute.map.CollectionBackedMapAttribute;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

abstract class CollectionBackedConvertingEntityMapAttribute<F extends EntityAttributes<K>, K, E extends Entity<K, ?>>
extends CollectionBackedMapAttribute<F, K, E>
implements EntityMapAttribute<K, E> {

    CollectionBackedConvertingEntityMapAttribute(Collection<F> collection) {
        super(collection);
    }

    @Override
    public EditableEntityMap<K, E> value() {
        return new SimpleEntityMap<>(super.value());
    }
}