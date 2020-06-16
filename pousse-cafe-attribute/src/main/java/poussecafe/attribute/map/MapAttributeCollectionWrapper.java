package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import poussecafe.attribute.MapAttribute;

public abstract class MapAttributeCollectionWrapper<K, D, E extends D> implements MapAttribute<K, D> {

    public MapAttributeCollectionWrapper(Collection<E> collection) {
        Objects.requireNonNull(collection);
        this.wrappedCollection = collection;
        buildMap();
    }

    private Collection<E> wrappedCollection;

    private void buildMap() {
        map = new HashMap<>();
        for(D data : wrappedCollection) {
            map.put(extractKey(data), data);
        }
    }

    private Map<K, D> map;

    protected abstract K extractKey(D data);

    @Override
    public Map<K, D> value() {
        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void value(Map<K, D> value) {
        Objects.requireNonNull(value);
        wrappedCollection.clear();
        for(D data : value.values()) {
            wrappedCollection.add((E) data);
        }
        buildMap();
    }

    @Override
    public Optional<D> get(K key) {
        if(map.containsKey(key)) {
            return Optional.of(map.get(key));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public D put(K key,
            D value) {
        if(map.containsKey(key)) {
            remove(key);
        }
        K extractedKey = extractKey(value);
        if(!extractedKey.equals(key)) {
            throw new IllegalArgumentException("Key does not match value");
        }
        wrappedCollection.add((E) value);
        return map.put(key, value);
    }

    @Override
    public Collection<D> values() {
        return Collections.unmodifiableCollection(wrappedCollection);
    }

    @Override
    public D remove(K key) {
        if(map.containsKey(key)) {
            D removedData = map.remove(key);
            wrappedCollection.remove(removedData);
            return removedData;
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return wrappedCollection.isEmpty();
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
    public Stream<D> valuesStream() {
        return map.values().stream();
    }

    @Override
    public Set<Entry<K, D>> entrySet() {
        return Collections.unmodifiableSet(map.entrySet());
    }
}
