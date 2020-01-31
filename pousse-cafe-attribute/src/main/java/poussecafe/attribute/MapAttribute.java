package poussecafe.attribute;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface MapAttribute<K, V> extends Attribute<Map<K, V>> {

    Optional<V> get(K key);

    V put(K key, V value);

    Collection<V> values();

    V remove(K key);

    boolean isEmpty();

    boolean containsKey(K key);

    Set<K> keySet();

    Stream<V> valuesStream();

    Set<Entry<K, V>> entrySet();

    int size();

    void clear();

    void putAll(Map<K, V> map);
}
