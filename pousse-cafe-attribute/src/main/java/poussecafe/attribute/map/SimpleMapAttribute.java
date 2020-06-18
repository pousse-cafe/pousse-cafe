package poussecafe.attribute.map;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import poussecafe.attribute.MapAttribute;

class SimpleMapAttribute<K, V> implements MapAttribute<K, V> {

    SimpleMapAttribute(Map<K, V> wrappedMap) {
        Objects.requireNonNull(wrappedMap);
        this.wrappedMap = wrappedMap;
    }

    private Map<K, V> wrappedMap;

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(wrappedMap.get(key));
    }

    @Override
    public EditableMap<K, V> mutableValue() {
        return new SimpleEditableMap<>(wrappedMap);
    }
}
