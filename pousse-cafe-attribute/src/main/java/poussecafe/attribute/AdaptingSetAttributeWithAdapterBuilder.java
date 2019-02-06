package poussecafe.attribute;

import java.util.Set;

public class AdaptingSetAttributeWithAdapterBuilder<U, T> {

    AdaptingSetAttributeWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    public CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> withSet(Set<U> storageSet) {
        CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> builder = new CompleteAdaptingSetAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.storageSet = storageSet;
        return builder;
    }
}
