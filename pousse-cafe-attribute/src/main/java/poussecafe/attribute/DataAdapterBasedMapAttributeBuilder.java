package poussecafe.attribute;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.MapAttributeBuilder.Complete;
import poussecafe.attribute.MapAttributeBuilder.ExpectingMap;
import poussecafe.attribute.adapters.DataAdapter;

public class DataAdapterBasedMapAttributeBuilder<J, U, K, V>
implements ExpectingMap<J, U, K, V>, Complete<K, V> {

    DataAdapterBasedMapAttributeBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    @Override
    public Complete<K, V> withMap(Map<J, U> storageMap) {
        Objects.requireNonNull(storageMap);
        this.storageMap = storageMap;
        return this;
    }

    Map<J, U> storageMap;

    @Override
    public MapAttribute<K, V> build() {
        return new ConvertingMapAttribute<>(storageMap) {
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
