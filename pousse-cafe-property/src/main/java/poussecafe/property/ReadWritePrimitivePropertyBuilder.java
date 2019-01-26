package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class ReadWritePrimitivePropertyBuilder<U, T> {

    ReadWritePrimitivePropertyBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public Property<T> build() {
        return new Property<T>() {
            @Override
            public T get() {
                return adapter.adaptGet(getter.get());
            }

            @Override
            public void set(T value) {
                setter.accept(adapter.adaptSet(value));
            }
        };
    }
}
