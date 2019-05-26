package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;

public class OptionalAttributeBuilder<T> {

    OptionalAttributeBuilder() {

    }

    Class<T> propertyTypeClass;

    public ReadOnlyOptionalAttributeBuilder<T> read(Supplier<T> getter) {
        Objects.requireNonNull(getter);
        return new ReadOnlyOptionalAttributeBuilder<>(getter);
    }

    public <U> AdaptingOptionalAttributeBuilder<U, T> storedAs(Class<U> storedType) {
        return new AdaptingOptionalAttributeBuilder<>();
    }

    public <U> AdaptingOptionalAttributeWithAdapterBuilder<U, T> usingDataAdapter(DataAdapter<U, T> adapter) {
        Objects.requireNonNull(adapter);
        AdaptingOptionalAttributeWithAdapterBuilder<U, T> builder = new AdaptingOptionalAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        return builder;
    }

    public <U> AdaptingOptionalAttributeWithAdapterBuilder<U, T> usingAutoAdapter(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        return usingDataAdapter(DataAdapters.auto(propertyTypeClass, autoAdapterClass));
    }
}
