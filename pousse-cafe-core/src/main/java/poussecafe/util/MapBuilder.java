package poussecafe.util;

import java.util.Map;
import java.util.function.Function;

public class MapBuilder<K, V> {

    private Map<K, V> map;

    private Function<K, V> valueBuilder;

    public MapBuilder(Function<K, V> valueBuilder) {
        this.valueBuilder = valueBuilder;
    }

    public V getOrCreate(K key) {
        V value = map.get(key);
        if(value == null) {
            value = valueBuilder.apply(key);
        }
        return value;
    }

    public Map<K, V> buildMap() {
        return map;
    }

}
