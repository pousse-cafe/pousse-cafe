package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class ReadOnlyPrimitivePropertyBuilder<U, T> {

    ReadOnlyPrimitivePropertyBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    public ReadWritePrimitivePropertyBuilder<U, T> set(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        ReadWritePrimitivePropertyBuilder<U, T> builder = new ReadWritePrimitivePropertyBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
