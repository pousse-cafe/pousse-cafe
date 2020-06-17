package poussecafe.collection;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MapEditor<K, V> {

    public MapEditor(Map<K, V> map) {
        requireNonNull(map);
        this.map = map;
    }

    private Map<K, V> map;

    public MapEditor<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapEditor<K, V> remove(K key) {
        map.remove(key);
        return this;
    }

    public Map<K, V> finish() {
        return map;
    }
}