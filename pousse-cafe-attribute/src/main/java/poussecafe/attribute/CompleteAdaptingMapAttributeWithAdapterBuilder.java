package poussecafe.attribute;

import java.util.Map;
import poussecafe.attribute.adapters.DataAdapter;

public class CompleteAdaptingMapAttributeWithAdapterBuilder<J, U, K, V> {

    CompleteAdaptingMapAttributeWithAdapterBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    Map<J, U> storageMap;

    public MapAttribute<K, V> build() {
        return new ConvertingMapAttribute<J, U, K, V>(storageMap) {
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
