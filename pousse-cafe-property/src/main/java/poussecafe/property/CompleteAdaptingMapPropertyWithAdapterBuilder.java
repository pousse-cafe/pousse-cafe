package poussecafe.property;

import java.util.Map;
import poussecafe.property.adapters.DataAdapter;

public class CompleteAdaptingMapPropertyWithAdapterBuilder<J, U, K, V> {

    CompleteAdaptingMapPropertyWithAdapterBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    Map<J, U> storageMap;

    public MapProperty<K, V> build() {
        return new ConvertingMapProperty<J, U, K, V>(storageMap) {
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
