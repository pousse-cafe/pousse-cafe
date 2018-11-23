package poussecafe.property;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class PrimitivePropertyBuilder<U, T> {

    PrimitivePropertyBuilder(DataAdapter<U, T> adapter) {
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    public ReadOnlyPrimitivePropertyBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        ReadOnlyPrimitivePropertyBuilder<U, T> builder = new ReadOnlyPrimitivePropertyBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        return builder;
    }

}
