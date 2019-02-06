package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;

public class OptionalAttributeBuilder<T> {

    OptionalAttributeBuilder() {

    }

    Class<T> propertyTypeClass;

    public ReadOnlyOptionalAttributeBuilder<T> get(Supplier<T> getter) {
        Objects.requireNonNull(getter);
        return new ReadOnlyOptionalAttributeBuilder<>(getter);
    }

    public <U> AdaptingOptionalAttributeBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingOptionalAttributeBuilder<>();
    }

    public <U> AdaptingOptionalAttributeWithAdapterBuilder<U, T> withDataAdapter(DataAdapter<U, T> adapter) {
        Objects.requireNonNull(adapter);
        AdaptingOptionalAttributeWithAdapterBuilder<U, T> builder = new AdaptingOptionalAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        return builder;
    }

    public <U> AdaptingOptionalAttributeWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        return withDataAdapter(DataAdapters.auto(propertyTypeClass, autoAdapterClass));
    }
}
