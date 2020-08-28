package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import poussecafe.collection.MapEditor;

import static java.util.Objects.requireNonNull;

public class SimpleEditableMap<K, V> implements EditableMap<K, V> {

    public SimpleEditableMap(Map<K, V> map) {
        requireNonNull(map);
        this.map = map;
    }

    private Map<K, V> map;

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
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
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
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public MapEditor<K, V> edit() {
        return new MapEditor<>(map);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Map) {
            return map.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
