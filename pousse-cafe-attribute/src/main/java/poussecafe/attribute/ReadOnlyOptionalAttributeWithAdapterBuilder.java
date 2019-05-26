package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadOnlyOptionalAttributeWithAdapterBuilder<U, T> {

    ReadOnlyOptionalAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    public ReadWriteOptionalAttributeWithAdapterBuilder<U, T> write(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        ReadWriteOptionalAttributeWithAdapterBuilder<U, T> builder = new ReadWriteOptionalAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
