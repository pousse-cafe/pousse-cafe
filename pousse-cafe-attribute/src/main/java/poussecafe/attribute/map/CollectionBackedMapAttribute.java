package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.CollectionBackedAdaptingMap;
import poussecafe.attribute.adapters.DataAdapters;

public abstract class CollectionBackedMapAttribute<U, K, V> implements MapAttribute<K, V> {

    public CollectionBackedMapAttribute(Collection<U> collection) {
        map = new CollectionBackedAdaptingMap.Builder<U, K, V>()
                .collection(collection)
                .adapter(DataAdapters.adapter(this::convertFromValue, this::convertToValue))
                .build();
    }

    private CollectionBackedAdaptingMap<U, K, V> map;

    protected abstract Entry<K, V> convertFromValue(U from);

    protected abstract U convertToValue(Entry<K, V> from);

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public EditableMap<K, V> value() {
        return map;
    }
}
