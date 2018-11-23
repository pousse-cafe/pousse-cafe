package poussecafe.property;

import java.util.Objects;

public class ListPropertyBuilder<T> {

    ListPropertyBuilder() {

    }

    Class<T> elementClass;

    public ReadOnlyListPropertyBuilder<T> read() {
        return new ReadOnlyListPropertyBuilder<>();
    }

    public <U> AdaptingListPropertyBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingListPropertyBuilder<>();
    }

    public <U> AdaptingListPropertyWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AdaptingListPropertyWithAdapterBuilder<U, T> builder = new AdaptingListPropertyWithAdapterBuilder<>();
        builder.adapter = new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass);
        return builder;
    }
}
