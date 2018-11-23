package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class ReadOnlyOptionalPropertyWithAdapterBuilder<U, T> {

    ReadOnlyOptionalPropertyWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    public ReadWriteOptionalPropertyWithAdapterBuilder<U, T> set(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        ReadWriteOptionalPropertyWithAdapterBuilder<U, T> builder = new ReadWriteOptionalPropertyWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
