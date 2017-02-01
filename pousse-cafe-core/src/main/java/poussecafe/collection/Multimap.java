package poussecafe.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;

public class Multimap<K, V> {

    private Map<K, Set<V>> map;

    public Multimap() {
        map = new HashMap<>();
    }

    public void put(K key,
            V value) {
        getForUpdate(key).add(value);
    }

    private Set<V> getForUpdate(K key) {
        Set<V> values = map.get(key);
        if (values == null) {
            values = new HashSet<>();
            map.put(key, values);
        }
        return values;
    }

    public Set<V> get(K key) {
        return Optional.ofNullable(map.get(key)).orElse(emptySet());
    }

    public void remove(K key,
            V value) {
        getForUpdate(key).remove(value);
    }

    public void putAll(K key,
            Set<V> values) {
        getForUpdate(key).addAll(values);
    }

}
