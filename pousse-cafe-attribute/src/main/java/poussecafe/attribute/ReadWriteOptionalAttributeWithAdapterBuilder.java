package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class ReadWriteOptionalAttributeWithAdapterBuilder<U, T> {

    ReadWriteOptionalAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public OptionalAttribute<T> build() {
        return new OptionalAttribute<T>() {
            @Override
            public T getNullable() {
                U storedValue = getter.get();
                if(storedValue == null) {
                    return null;
                } else {
                    return adapter.adaptGet(storedValue);
                }
            }

            @Override
            public void setNullable(T nullableValue) {
                if(nullableValue == null) {
                    setter.accept(null);
                } else {
                    setter.accept(adapter.adaptSet(nullableValue));
                }
            }
        };
    }
}
