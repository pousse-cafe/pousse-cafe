package poussecafe.attribute.entity;

import poussecafe.attribute.map.EditableMap;

public interface EditableEntityMap<K, E>
extends EditableMap<K, E> {

    E putEntity(E entity);
}
