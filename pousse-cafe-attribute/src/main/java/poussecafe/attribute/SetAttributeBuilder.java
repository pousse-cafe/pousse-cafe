package poussecafe.attribute;

import java.util.Objects;
import java.util.Set;
import poussecafe.attribute.adapters.DataAdapter;

public class SetAttributeBuilder<T> {

    SetAttributeBuilder() {

    }

    Class<T> elementClass;

    public ReadWriteSetAttributeBuilder<T> withSet(Set<T> set) {
        Objects.requireNonNull(set);
        return new ReadWriteSetAttributeBuilder<>(set);
    }

    public <U> AdaptingSetAttributeBuilder<U, T> itemsStoredAs(Class<U> storedElementType) {
        return new AdaptingSetAttributeBuilder<>();
    }

    public <U> AdaptingSetAttributeWithAdapterBuilder<U, T> usingItemAutoAdapter(Class<U> dataAdapterClass) {
        return usingItemDataAdapter(new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass));
    }

    public <U> AdaptingSetAttributeWithAdapterBuilder<U, T> usingItemDataAdapter(DataAdapter<U, T> dataAdapter) {
        Objects.requireNonNull(dataAdapter);
        AdaptingSetAttributeWithAdapterBuilder<U, T> builder = new AdaptingSetAttributeWithAdapterBuilder<>();
        builder.adapter = dataAdapter;
        return builder;
    }
}
