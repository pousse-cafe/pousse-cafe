package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadOnlyPrimitiveAttributeBuilder<U, T> {

    ReadOnlyPrimitiveAttributeBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    public ReadWritePrimitiveAttributeBuilder<U, T> set(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        ReadWritePrimitiveAttributeBuilder<U, T> builder = new ReadWritePrimitiveAttributeBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
