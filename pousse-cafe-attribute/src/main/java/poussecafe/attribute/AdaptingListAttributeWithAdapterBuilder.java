package poussecafe.attribute;

import java.util.List;
import poussecafe.attribute.adapters.DataAdapter;

public class AdaptingListAttributeWithAdapterBuilder<U, T> {

    AdaptingListAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    public CompleteAdaptingListAttributeWithAdapterBuilder<U, T> withList(List<U> storageList) {
        CompleteAdaptingListAttributeWithAdapterBuilder<U, T> builder = new CompleteAdaptingListAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.storageList = storageList;
        return builder;
    }
}
