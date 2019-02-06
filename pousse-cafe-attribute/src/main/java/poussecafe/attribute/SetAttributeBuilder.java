package poussecafe.attribute;

import java.util.Objects;
import java.util.Set;

public class SetAttributeBuilder<T> {

    SetAttributeBuilder() {

    }

    Class<T> elementClass;

    public ReadWriteSetAttributeBuilder<T> withSet(Set<T> set) {
        Objects.requireNonNull(set);
        return new ReadWriteSetAttributeBuilder<>(set);
    }

    public <U> AdaptingSetAttributeBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingSetAttributeBuilder<>();
    }

    public <U> AdaptingSetAttributeWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AdaptingSetAttributeWithAdapterBuilder<U, T> builder = new AdaptingSetAttributeWithAdapterBuilder<>();
        builder.adapter = new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass);
        return builder;
    }
}
