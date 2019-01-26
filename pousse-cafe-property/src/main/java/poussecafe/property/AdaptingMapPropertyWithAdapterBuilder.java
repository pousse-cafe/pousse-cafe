package poussecafe.property;

import java.util.Map;
import poussecafe.property.adapters.DataAdapter;

public class AdaptingMapPropertyWithAdapterBuilder<J, U, K, V> {

    AdaptingMapPropertyWithAdapterBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    public CompleteAdaptingMapPropertyWithAdapterBuilder<J, U, K, V> withMap(Map<J, U> storageMap) {
        CompleteAdaptingMapPropertyWithAdapterBuilder<J, U, K, V> builder = new CompleteAdaptingMapPropertyWithAdapterBuilder<>();
        builder.keyAdapter = keyAdapter;
        builder.valueAdapter = valueAdapter;
        builder.storageMap = storageMap;
        return builder;
    }
}
