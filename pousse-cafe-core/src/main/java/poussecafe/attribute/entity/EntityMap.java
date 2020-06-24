package poussecafe.attribute.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import poussecafe.attribute.map.EditableMap;
import poussecafe.collection.MapEditor;
import poussecafe.domain.Entity;

import static java.util.Objects.requireNonNull;

class EntityMap<K, E extends Entity<K, ?>> implements EditableEntityMap<K, E> {

    EntityMap(EditableMap<K, E> map) {
        requireNonNull(map);
        this.map = map;
    }

    private EditableMap<K, E> map;

    @Override
    public MapEditor<K, E> edit() {
        return map.edit();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public E get(Object key) {
        return map.get(key);
    }

    @Override
    public E put(K key, E value) {
        if(!key.equals(value.attributes().identifier().value())) {
            throw new IllegalArgumentException("Given entity's identifier does not match map key");
        }
        return map.put(key, value);
    }

    @Override
    public E remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends E> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<E> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, E>> entrySet() {
        return map.entrySet();
    }

    @Override
    public E putEntity(E entity) {
        return map.put(entity.attributes().identifier().value(), entity);
    }
}
