package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class PrimitiveAttributeBuilder<U, T> {

    PrimitiveAttributeBuilder(DataAdapter<U, T> adapter) {
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    public ReadOnlyPrimitiveAttributeBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        ReadOnlyPrimitiveAttributeBuilder<U, T> builder = new ReadOnlyPrimitiveAttributeBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        return builder;
    }

}
