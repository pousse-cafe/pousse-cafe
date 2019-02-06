package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadWritePrimitiveAttributeBuilder<U, T> {

    ReadWritePrimitiveAttributeBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public Attribute<T> build() {
        return new Attribute<T>() {
            @Override
            public T value() {
                return adapter.adaptGet(getter.get());
            }

            @Override
            public void value(T value) {
                setter.accept(adapter.adaptSet(value));
            }
        };
    }
}
