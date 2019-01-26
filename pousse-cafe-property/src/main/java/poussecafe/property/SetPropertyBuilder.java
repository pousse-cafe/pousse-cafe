package poussecafe.property;

import java.util.Objects;
import java.util.Set;

public class SetPropertyBuilder<T> {

    SetPropertyBuilder() {

    }

    Class<T> elementClass;

    public ReadWriteSetPropertyBuilder<T> withSet(Set<T> set) {
        Objects.requireNonNull(set);
        return new ReadWriteSetPropertyBuilder<>(set);
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
