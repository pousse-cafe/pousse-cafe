package poussecafe.attribute;

import java.util.Map;
import poussecafe.attribute.adapters.DataAdapter;

public class AdaptingMapAttributeWithAdapterBuilder<J, U, K, V> {

    AdaptingMapAttributeWithAdapterBuilder() {

    }

    DataAdapter<J, K> keyAdapter;

    DataAdapter<U, V> valueAdapter;

    public CompleteAdaptingMapAttributeWithAdapterBuilder<J, U, K, V> withMap(Map<J, U> storageMap) {
        CompleteAdaptingMapAttributeWithAdapterBuilder<J, U, K, V> builder = new CompleteAdaptingMapAttributeWithAdapterBuilder<>();
        builder.keyAdapter = keyAdapter;
        builder.valueAdapter = valueAdapter;
        builder.storageMap = storageMap;
        return builder;
    }
}
