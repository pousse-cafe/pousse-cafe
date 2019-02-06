package poussecafe.attribute;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;

public class MapAttributeBuilder<K, V> {

    MapAttributeBuilder() {

    }

    public ReadWriteMapAttributeBuilder<K, V> withMap(Map<K, V> map) {
        return new ReadWriteMapAttributeBuilder<>(map);
    }

    public <J, U> AdaptingMapAttributeBuilder<J, U, K, V> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingMapAttributeBuilder<>();
    }

    public <J, U> AdaptingMapAttributeWithAdapterBuilder<J, U, K, V> fromAdapting(DataAdapter<J, K> keyAdapter, DataAdapter<U, V> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        AdaptingMapAttributeWithAdapterBuilder<J, U, K, V> builder = new AdaptingMapAttributeWithAdapterBuilder<>();
        builder.keyAdapter = keyAdapter;
        builder.valueAdapter = valueAdapter;
        return builder;
    }
}
