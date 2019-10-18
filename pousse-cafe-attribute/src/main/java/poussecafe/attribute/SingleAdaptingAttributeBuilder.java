package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class SingleAdaptingAttributeBuilder<U, T> {

    SingleAdaptingAttributeBuilder(DataAdapter<U, T> adapter) {
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    public ReadOnlySingleAttributeBuilder<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        ReadOnlySingleAttributeBuilder<U, T> builder = new ReadOnlySingleAttributeBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        return builder;
    }

}
