package poussecafe.attribute;

import java.util.List;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;

public class ListAttributeBuilder<T> {

    ListAttributeBuilder() {

    }

    Class<T> elementClass;

    public ReadWriteListAttributeBuilder<T> withList(List<T> list) {
        Objects.requireNonNull(list);
        return new ReadWriteListAttributeBuilder<>(list);
    }

    public <U> AdaptingListAttributeBuilder<U, T> itemsStoredAs(Class<U> storedElementType) {
        return new AdaptingListAttributeBuilder<>();
    }

    public <U> AdaptingListAttributeWithAdapterBuilder<U, T> usingItemAutoAdapter(Class<U> dataAdapterClass) {
        return usingItemDataAdapter(new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass));
    }

    public <U> AdaptingListAttributeWithAdapterBuilder<U, T> usingItemDataAdapter(DataAdapter<U, T> dataAdapter) {
        Objects.requireNonNull(dataAdapter);
        AdaptingListAttributeWithAdapterBuilder<U, T> builder = new AdaptingListAttributeWithAdapterBuilder<>();
        builder.adapter = dataAdapter;
        return builder;
    }
}
