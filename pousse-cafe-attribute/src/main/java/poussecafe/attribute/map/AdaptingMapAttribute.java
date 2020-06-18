package poussecafe.attribute.map;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.AdaptingMap;
import poussecafe.attribute.adapters.DataAdapters;

public abstract class AdaptingMapAttribute<L, U, K, V> implements MapAttribute<K, V> {

    public AdaptingMapAttribute(Map<L, U> map) {
        Objects.requireNonNull(map);
        this.map = new AdaptingMap.Builder<L, U, K, V>()
                .mutableMap(map)
                .keyAdapter(DataAdapters.adapter(this::convertFromKey, this::convertToKey))
                .valueAdapter(DataAdapters.adapter(this::convertFromValue, this::convertToValue))
                .build();
    }

    protected abstract K convertFromKey(L from);

    protected abstract L convertToKey(K from);

    protected abstract V convertFromValue(U from);

    protected abstract U convertToValue(V from);

    private AdaptingMap<L, U, K, V> map;

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public EditableMap<K, V> mutableValue() {
        return map;
    }
}
