package poussecafe.attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

abstract class CollectionBackedMapAttribute<U, K, V> implements MapAttribute<K, V> {

    CollectionBackedMapAttribute(Collection<U> collection) {
        this.collection = collection;
    }

    Collection<U> collection;

    @Override
    public Map<K, V> value() {
        return Collections.unmodifiableMap(mutableMap());
    }

    private Map<K, V> mutableMap() {
        return collection.stream().collect(toConvertedFromMap());
    }

    private Collector<U, ?, Map<K, V>> toConvertedFromMap() {
        return toMap(item -> convertFromValue(item).getKey(), item -> convertFromValue(item).getValue());
    }

    protected abstract Entry<K, V> convertFromValue(U from);

    @Override
    public void value(Map<K, V> value) {
        Objects.requireNonNull(value);
        collection.clear();
        collection.addAll(value.entrySet().stream().map(this::convertToValue).collect(toList()));
    }

    protected abstract U convertToValue(Entry<K, V> from);

    @Override
    public Optional<V> get(K key) {
        return findItem(key).map(this::convertFromValue).map(Entry::getValue);
    }

    private Optional<U> findItem(K key) {
        return collection.stream()
                .filter(item -> convertFromValue(item).getKey().equals(key))
                .findFirst();
    }

    @Override
    public V put(K key, V value) {
        var mutableMap = mutableMap();
        var oldValue = mutableMap.put(key, value);
        value(mutableMap);
        return oldValue;
    }

    @Override
    public Collection<V> values() {
        return collection.stream().map(this::convertFromValue).map(Entry::getValue).collect(toList());
    }

    @Override
    public V remove(K key) {
        var mutableMap = mutableMap();
        var oldValue = mutableMap.remove(key);
        value(mutableMap);
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return findItem(key).isPresent();
    }

    @Override
    public Set<K> keySet() {
        return collection.stream().map(this::convertFromValue).map(Entry::getKey).collect(toSet());
    }

    @Override
    public Stream<V> valuesStream() {
        return values().stream();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return collection.stream().map(this::convertEntry).collect(toSet());
    }

    private Entry<K, V> convertEntry(U item) {
        return new ReadOnlyEntry<>(convertFromValue(item).getKey(), convertFromValue(item).getValue());
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public void putAll(Map<K, V> map) {
        map.entrySet().stream().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }
}
