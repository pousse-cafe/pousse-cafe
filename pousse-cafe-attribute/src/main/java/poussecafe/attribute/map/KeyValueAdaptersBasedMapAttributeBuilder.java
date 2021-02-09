package poussecafe.attribute.map;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.map.MapAttributeBuilder.Complete;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingMap;

class KeyValueAdaptersBasedMapAttributeBuilder<J, U, K, V>
implements ExpectingMap<J, U, K, V>, Complete<K, V> {

    KeyValueAdaptersBasedMapAttributeBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    @SuppressWarnings("unchecked")
    @Override
    public Complete<K, V> withMap(Map<J, ? extends U> storageMap) {
        Objects.requireNonNull(storageMap);
        this.storageMap = (Map<J, U>) storageMap;
        return this;
    }

    Map<J, U> storageMap;

    @Override
    public MapAttribute<K, V> build() {
        return new AdaptingMapAttribute<>(storageMap) {
            @Override
            protected K convertFromKey(J from) {
                return keyAdapter.adaptGet(from);
            }

            @Override
            protected V convertFromValue(U from) {
                return valueAdapter.adaptGet(from);
            }

            @Override
            protected J convertToKey(K from) {
                return keyAdapter.adaptSet(from);
            }

            @Override
            protected U convertToValue(V from) {
                return valueAdapter.adaptSet(from);
            }
        };
    }
}
