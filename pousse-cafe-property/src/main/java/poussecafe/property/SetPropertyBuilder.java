package poussecafe.property;

import java.util.Objects;

public class SetPropertyBuilder<T> {

    SetPropertyBuilder() {

    }

    Class<T> elementClass;

    public ReadOnlySetPropertyBuilder<T> read() {
        return new ReadOnlySetPropertyBuilder<>();
    }

    public <U> AdaptingSetPropertyBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingSetPropertyBuilder<>();
    }

    public <U> AdaptingSetPropertyWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AdaptingSetPropertyWithAdapterBuilder<U, T> builder = new AdaptingSetPropertyWithAdapterBuilder<>();
        builder.adapter = new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass);
        return builder;
    }
}
