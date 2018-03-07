package poussecafe.storable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface MapProperty<K, V> extends Property<Map<K, V>> {

    Optional<V> get(K key);

    void put(K key, V value);

    Collection<V> values();

    V remove(K key);

    boolean isEmpty();
}
