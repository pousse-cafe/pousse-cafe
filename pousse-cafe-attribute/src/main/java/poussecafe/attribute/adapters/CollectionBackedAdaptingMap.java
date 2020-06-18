package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import poussecafe.attribute.map.ImmutableEntry;
import poussecafe.collection.MapEditor;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class CollectionBackedAdaptingMap<U, K, V> implements EditableMap<K, V> {

    private void computeViews() {
        mapView = collection.stream().collect(toConvertedFromMap());

        values = new CollectionBackedAdaptingMapValues<>();
        values.collection = collection;
        values.adapter = adapter;
        values.mapView = mapView;

        keys = new CollectionBackedAdaptingMapKeys<>();
        keys.collection = collection;
        keys.adapter = adapter;
        keys.mapView = mapView;

        entries = new CollectionBackedAdaptingMapEntries<>();
        entries.collection = collection;
        entries.adapter = adapter;
        entries.mapView = mapView;
        entries.mutableMap = this;
    }

    private Collection<U> collection;

    private Map<K, V> mapView;

    private CollectionBackedAdaptingMapValues<U, K, V> values;

    private CollectionBackedAdaptingMapKeys<U, K, V> keys;

    private CollectionBackedAdaptingMapEntries<U, K, V> entries;

    private Collector<U, ?, Map<K, V>> toConvertedFromMap() {
        return toMap(item -> adapter.adaptGet(item).getKey(),
                item -> adapter.adaptGet(item).getValue());
    }

    private DataAdapter<U, Entry<K, V>> adapter;

    @Override
    public V get(Object key) {
        return mapView.get(key);
    }

    @Override
    public V put(K key, V value) {
        var oldValue = remove(key);
        mapView.put(key, value);
        collection.add(adapter.adaptSet(new ImmutableEntry<>(key, value)));
        return oldValue;
    }

    @Override
    public Collection<V> values() {
        return values;
    }

    @Override
    public V remove(Object key) {
        var oldValue = mapView.get(key);
        keys.remove(key);
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mapView.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entries;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear();
        mapView.clear();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.entrySet().stream().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public boolean containsValue(Object value) {
        return values.contains(value);
    }

    @Override
    public MapEditor<K, V> edit() {
        return new MapEditor<>(this);
    }

    public static class Builder<U, K, V> {

        private CollectionBackedAdaptingMap<U, K, V> map = new CollectionBackedAdaptingMap<>();

        public CollectionBackedAdaptingMap<U, K, V> build() {
            requireNonNull(map.collection);
            requireNonNull(map.adapter);
            map.computeViews();
            return map;
        }

        public Builder<U, K, V> collection(Collection<U> collection) {
            map.collection = collection;
            return this;
        }

        public Builder<U, K, V> adapter(DataAdapter<U, Entry<K, V>> adapter) {
            map.adapter = adapter;
            return this;
        }
    }

    private CollectionBackedAdaptingMap() {

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Map) {
            return mapView.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return mapView.hashCode();
    }

    @Override
    public String toString() {
        return mapView.toString();
    }
}
