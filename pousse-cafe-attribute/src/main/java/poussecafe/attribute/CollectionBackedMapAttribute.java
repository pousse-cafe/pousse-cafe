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

abstract class CollectionBackedMapAttribute<U, K, V> implements MapAttribute<K, V> {

    CollectionBackedMapAttribute(Collection<U> collection) {
        this.collection = collection;
    }

    Collection<U> collection;

    private Map<K, V> view;

    @Override
    public Map<K, V> value() {
        return Collections.unmodifiableMap(computeViewIfAbsent());
    }

    private Map<K, V> computeViewIfAbsent() {
        if(view == null) {
            view = collection.stream().collect(toConvertedFromMap());
            return view;
        } else {
            return view;
        }
    }

    private Collector<U, ?, Map<K, V>> toConvertedFromMap() {
        return toMap(item -> convertFromValue(item).getKey(), item -> convertFromValue(item).getValue());
    }

    protected abstract Entry<K, V> convertFromValue(U from);

    @Override
    public void value(Map<K, V> value) {
        value(value, true);
    }

    private void value(Map<K, V> value, boolean clearView) {
        Objects.requireNonNull(value);
        collection.clear();
        collection.addAll(value.entrySet().stream().map(this::convertToValue).collect(toList()));
        if(clearView) {
            view = null;
        }
    }

    protected abstract U convertToValue(Entry<K, V> from);

    @Override
    public Optional<V> get(K key) {
        computeViewIfAbsent();
        return Optional.ofNullable(view.get(key));
    }

    @Override
    public V put(K key, V value) {
        computeViewIfAbsent();
        var oldValue = view.put(key, value);
        if(oldValue == null) {
            collection.add(convertToValue(new ReadOnlyEntry<>(key, value)));
        } else {
            value(view, false);
        }
        return oldValue;
    }

    @Override
    public Collection<V> values() {
        return computeViewIfAbsent().values().stream().collect(toList());
    }

    @Override
    public V remove(K key) {
        computeViewIfAbsent();
        var oldValue = view.remove(key);
        if(oldValue != null) {
            value(view, false);
        }
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        computeViewIfAbsent();
        return view.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(computeViewIfAbsent().keySet());
    }

    @Override
    public Stream<V> valuesStream() {
        return values().stream();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(computeViewIfAbsent().entrySet());
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear();
        view.clear();
    }

    @Override
    public void putAll(Map<K, V> map) {
        map.entrySet().stream().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }
}
