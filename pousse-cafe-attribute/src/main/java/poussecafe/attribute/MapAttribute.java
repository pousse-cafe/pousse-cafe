package poussecafe.attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import poussecafe.attribute.adapters.EditableMap;

import static java.util.Objects.requireNonNull;

public interface MapAttribute<K, V> extends Attribute<Map<K, V>> {

    Optional<V> get(K key);

    EditableMap<K, V> mutableValue();

    @Override
    default Map<K, V> value() {
        return Collections.unmodifiableMap(mutableValue());
    }

    @Override
    default void value(Map<K, V> value) {
        requireNonNull(value);
        mutableValue().clear();
        mutableValue().putAll(value);
    }

    /**
     * @deprecated use mutableValue().put(key, Value)
     */
    @Deprecated(since = "0.19")
    default V put(K key, V value) {
        return mutableValue().put(key, value);
    }

    /**
     * @deprecated use value().values()
     */
    @Deprecated(since = "0.19")
    default Collection<V> values() {
        return value().values();
    }

    /**
     * @deprecated use mutableValue().remove(key)
     */
    @Deprecated(since = "0.19")
    default V remove(K key) {
        return mutableValue().remove(key);
    }

    /**
     * @deprecated use value().isEmpty()
     */
    @Deprecated(since = "0.19")
    default boolean isEmpty() {
        return value().isEmpty();
    }

    /**
     * @deprecated use value().containsKey(key)
     */
    @Deprecated(since = "0.19")
    default boolean containsKey(K key) {
        return value().containsKey(key);
    }

    /**
     * @deprecated use value().keySet()
     */
    @Deprecated(since = "0.19")
    default Set<K> keySet() {
        return value().keySet();
    }

    /**
     * @deprecated use values().stream()
     */
    @Deprecated(since = "0.19")
    default Stream<V> valuesStream() {
        return values().stream();
    }

    /**
     * @deprecated use value().entrySet()
     */
    @Deprecated(since = "0.19")
    default Set<Entry<K, V>> entrySet() {
        return value().entrySet();
    }

    /**
     * @deprecated use value().size()
     */
    @Deprecated(since = "0.19")
    default int size() {
        return value().size();
    }

    /**
     * @deprecated use mutableValue().clear()
     */
    @Deprecated(since = "0.19")
    default void clear() {
        mutableValue().clear();
    }

    /**
     * @deprecated use mutableValue().putAll(map)
     */
    @Deprecated(since = "0.19")
    default void putAll(Map<K, V> map) {
        mutableValue().putAll(map);
    }
}
