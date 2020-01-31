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

public abstract class ConvertingMapAttribute<L, U, K, V> implements MapAttribute<K, V> {

    public ConvertingMapAttribute(Map<L, U> map) {
        Objects.requireNonNull(map);
        this.map = map;
    }

    private Map<L, U> map;

    @Override
    public Map<K, V> value() {
        return Collections.unmodifiableMap(map.entrySet().stream().collect(toConvertedFromMap()));
    }

    private Collector<Entry<L, U>, ?, Map<K, V>> toConvertedFromMap() {
        return toMap(this::convertFromEntryKey, this::convertFromEntryValue);
    }

    private K convertFromEntryKey(Entry<L, U> entry) {
        return convertFromKey(entry.getKey());
    }

    protected abstract K convertFromKey(L from);

    private V convertFromEntryValue(Entry<L, U> entry) {
        return convertFromValue(entry.getValue());
    }

    protected abstract V convertFromValue(U from);

    @Override
    public void value(Map<K, V> value) {
        Objects.requireNonNull(value);
        map.clear();
        map.putAll(value.entrySet().stream().collect(toConvertedToMap()));
    }

    private Collector<Entry<K, V>, ?, Map<L, U>> toConvertedToMap() {
        return toMap(this::convertToEntryKey, this::convertToEntryValue);
    }

    private L convertToEntryKey(Entry<K, V> entry) {
        return convertToKey(entry.getKey());
    }

    protected abstract L convertToKey(K from);

    private U convertToEntryValue(Entry<K, V> entry) {
        return convertToValue(entry.getValue());
    }

    protected abstract U convertToValue(V from);

    @Override
    public Optional<V> get(K key) {
        U value = map.get(convertToKey(key));
        if(value == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertFromValue(value));
        }
    }

    @Override
    public V put(K key,
            V value) {
        L fromKey = convertToKey(key);
        U fromValue = convertToValue(value);
        U oldValue = map.put(fromKey, fromValue);
        if(oldValue == null) {
            return null;
        } else {
            return convertFromValue(oldValue);
        }
    }

    @Override
    public Collection<V> values() {
        return map.values().stream().map(this::convertFromValue).collect(toList());
    }

    @Override
    public V remove(K key) {
        U removed = map.remove(convertToKey(key));
        if(removed != null) {
            return convertFromValue(removed);
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return keySet().contains(key);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet().stream().map(this::convertFromKey).collect(toSet());
    }

    @Override
    public Stream<V> valuesStream() {
        return map.values().stream().map(this::convertFromValue);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet().stream().map(this::convertEntry).collect(toSet());
    }

    private Entry<K, V> convertEntry(Entry<L, U> entry) {
        return new ReadOnlyEntry<>(convertFromKey(entry.getKey()), convertFromValue(entry.getValue()));
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
    public void putAll(Map<K, V> value) {
        map.putAll(value.entrySet().stream().collect(toConvertedToMap()));
    }
}
