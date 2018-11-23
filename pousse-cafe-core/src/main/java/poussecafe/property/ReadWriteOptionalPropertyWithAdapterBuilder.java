package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class ReadWriteOptionalPropertyWithAdapterBuilder<U, T> {

    ReadWriteOptionalPropertyWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public OptionalProperty<T> build() {
        return new OptionalProperty<T>() {
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
