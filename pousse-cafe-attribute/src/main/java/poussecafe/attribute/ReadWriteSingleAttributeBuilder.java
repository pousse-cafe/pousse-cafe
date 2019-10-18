package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadWriteSingleAttributeBuilder<U, T> {

    ReadWriteSingleAttributeBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public Attribute<T> build() {
        return new Attribute<>() {
            @Override
            public T value() {
                return adapter.adaptGet(getter.get());
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                setter.accept(adapter.adaptSet(value));
            }
        };
    }
}
