package poussecafe.storable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static poussecafe.check.Checks.checkThatValue;

public class SimpleMapProperty<K, V> implements MapProperty<K, V> {

    public SimpleMapProperty(Map<K, V> wrappedMap) {
        checkThatValue(wrappedMap).notNull();
        this.wrappedMap = wrappedMap;
    }

    private Map<K, V> wrappedMap;

    @Override
    public Map<K, V> get() {
        return Collections.unmodifiableMap(wrappedMap);
    }

    @Override
    public void set(Map<K, V> value) {
        wrappedMap.clear();
        wrappedMap.putAll(value);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(wrappedMap.get(key));
    }

    @Override
    public void put(K key,
            V value) {
        wrappedMap.put(key, value);
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(wrappedMap.values());
    }

    @Override
    public V remove(K key) {
        return wrappedMap.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return wrappedMap.isEmpty();
    }

}
