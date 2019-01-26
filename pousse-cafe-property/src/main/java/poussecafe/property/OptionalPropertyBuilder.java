package poussecafe.property;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class OptionalPropertyBuilder<T> {

    public ReadOnlyOptionalPropertyBuilder<T> get(Supplier<T> getter) {
        Objects.requireNonNull(getter);
        return new ReadOnlyOptionalPropertyBuilder<>(getter);
    }

    public <U> AdaptingOptionalPropertyBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingOptionalPropertyBuilder<>();
    }

    public <U> AdaptingOptionalPropertyWithAdapterBuilder<U, T> withDataAdapter(DataAdapter<U, T> adapter) {
        Objects.requireNonNull(adapter);
        AdaptingOptionalPropertyWithAdapterBuilder<U, T> builder = new AdaptingOptionalPropertyWithAdapterBuilder<>();
        builder.adapter = adapter;
        return builder;
    }
}
