package poussecafe.attribute;

import java.util.List;
import java.util.Objects;

public class ListAttributeBuilder<T> {

    ListAttributeBuilder() {

    }

    Class<T> elementClass;

    public ReadWriteListAttributeBuilder<T> withList(List<T> list) {
        Objects.requireNonNull(list);
        return new ReadWriteListAttributeBuilder<>(list);
    }

    public <U> AdaptingListAttributeBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingListAttributeBuilder<>();
    }

    public <U> AdaptingListAttributeWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AdaptingListAttributeWithAdapterBuilder<U, T> builder = new AdaptingListAttributeWithAdapterBuilder<>();
        builder.adapter = new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass);
        return builder;
    }
}
