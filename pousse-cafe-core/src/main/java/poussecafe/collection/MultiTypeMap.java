package poussecafe.collection;

import java.util.HashMap;
import java.util.Optional;

public class MultiTypeMap<K> {

    public void put(K key, Object value) {
        if(value == null) {
            throw new NullPointerException();
        }
        objectMap.put(key, value);
    }

    private HashMap<K, Object> objectMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(K key) {
        T value = (T) objectMap.get(key);
        return Optional.ofNullable(value);
    }

    public static <L> MultiTypeMap<L> empty() {
        return new MultiTypeMap<>();
    }
}
