package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.CollectionBackedAdaptingMap;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.attribute.adapters.EditableMap;

abstract class CollectionBackedMapAttribute<U, K, V> implements MapAttribute<K, V> {

    CollectionBackedMapAttribute(Collection<U> collection) {
        map = new CollectionBackedAdaptingMap.Builder<U, K, V>()
                .collection(collection)
                .adapter(DataAdapters.adapter(this::convertFromValue, this::convertToValue))
                .build();
    }

    private CollectionBackedAdaptingMap<U, K, V> map;

    @Override
    public Map<K, V> value() {
        return Collections.unmodifiableMap(map);
    }

    protected abstract Entry<K, V> convertFromValue(U from);

    @Override
    public void value(Map<K, V> value) {
        map.clear();
        map.putAll(value);
    }

    protected abstract U convertToValue(Entry<K, V> from);

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Stream<V> valuesStream() {
        return values().stream();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(map.entrySet());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void putAll(Map<K, V> map) {
        this.map.putAll(map);
    }

    @Override
    public EditableMap<K, V> mutableValue() {
        return map;
    }
}
