package poussecafe.property;

import java.util.List;

public class AdaptingListPropertyWithAdapterBuilder<U, T> {

    AdaptingListPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    public CompleteAdaptingListPropertyWithAdapterBuilder<U, T> withList(List<U> storageList) {
        CompleteAdaptingListPropertyWithAdapterBuilder<U, T> builder = new CompleteAdaptingListPropertyWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.storageList = storageList;
        return builder;
    }
}
