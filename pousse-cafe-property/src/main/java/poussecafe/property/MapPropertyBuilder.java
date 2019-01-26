package poussecafe.property;

import java.util.Map;
import java.util.Objects;
import poussecafe.property.adapters.DataAdapter;

public class MapPropertyBuilder<K, V> {

    MapPropertyBuilder() {

    }

    public ReadWriteMapPropertyBuilder<K, V> withMap(Map<K, V> map) {
        return new ReadWriteMapPropertyBuilder<>(map);
    }

    public <J, U> AdaptingMapPropertyBuilder<J, U, K, V> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingMapPropertyBuilder<>();
    }

    public <J, U> AdaptingMapPropertyWithAdapterBuilder<J, U, K, V> fromAdapting(DataAdapter<J, K> keyAdapter, DataAdapter<U, V> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        AdaptingMapPropertyWithAdapterBuilder<J, U, K, V> builder = new AdaptingMapPropertyWithAdapterBuilder<>();
        builder.keyAdapter = keyAdapter;
        builder.valueAdapter = valueAdapter;
        return builder;
    }
}
