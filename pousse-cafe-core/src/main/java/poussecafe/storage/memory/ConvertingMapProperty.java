package poussecafe.storage.memory;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collector;
import poussecafe.storable.MapProperty;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ConvertingMapProperty<L, U, K, V> implements MapProperty<K, V> {

    public ConvertingMapProperty(Map<L, U> map) {
        checkThat(value(map).notNull());
        this.map = map;
    }

    private Map<L, U> map;

    @Override
    public Map<K, V> get() {
        return map.entrySet().stream().collect(toConvertedFromMap());
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
    public void set(Map<K, V> value) {
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
    public void put(K key,
            V value) {
        L fromKey = convertToKey(key);
        U fromValue = convertToValue(value);
        map.put(fromKey, fromValue);
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
}
