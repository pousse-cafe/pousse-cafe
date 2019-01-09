package poussecafe.property;

import java.util.Set;

public class AdaptingSetPropertyWithAdapterBuilder<U, T> {

    AdaptingSetPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    public CompleteAdaptingSetPropertyWithAdapterBuilder<U, T> withSet(Set<U> storageSet) {
        CompleteAdaptingSetPropertyWithAdapterBuilder<U, T> builder = new CompleteAdaptingSetPropertyWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.storageSet = storageSet;
        return builder;
    }
}
