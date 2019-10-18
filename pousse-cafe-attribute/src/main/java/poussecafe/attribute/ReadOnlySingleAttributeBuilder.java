package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadOnlySingleAttributeBuilder<U, T> {

    ReadOnlySingleAttributeBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    public ReadWriteSingleAttributeBuilder<U, T> write(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        ReadWriteSingleAttributeBuilder<U, T> builder = new ReadWriteSingleAttributeBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
