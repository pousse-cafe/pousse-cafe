package poussecafe.attribute;

import java.util.Set;
import poussecafe.attribute.adapters.DataAdapter;

public class AdaptingSetAttributeWithAdapterBuilder<U, T> {

    AdaptingSetAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    public CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> withSet(Set<U> storageSet) {
        CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> builder = new CompleteAdaptingSetAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.storageSet = storageSet;
        return builder;
    }
}
