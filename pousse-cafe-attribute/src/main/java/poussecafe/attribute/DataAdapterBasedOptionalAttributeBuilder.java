package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.OptionalAttributeBuilder.Complete;
import poussecafe.attribute.OptionalAttributeBuilder.ExpectingAdaptedReader;
import poussecafe.attribute.OptionalAttributeBuilder.ExpectingWriterAfterAdapter;
import poussecafe.attribute.adapters.DataAdapter;

import static java.util.Objects.requireNonNull;

class DataAdapterBasedOptionalAttributeBuilder<U, T>
implements ExpectingAdaptedReader<U, T>, ExpectingWriterAfterAdapter<U, T>, Complete<T> {

    DataAdapterBasedOptionalAttributeBuilder(DataAdapter<U, T> adapter) {
        requireNonNull(adapter);
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    @Override
    public ExpectingWriterAfterAdapter<U, T> read(Supplier<U> getter) {
        requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    private Supplier<U> getter;

    @Override
    public Complete<T> write(Consumer<U> setter) {
        requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<U> setter;

    @Override
    public OptionalAttribute<T> build() {
        return new OptionalAttribute<>() {
            @Override
            public T nullableValue() {
                U storedValue = getter.get();
                if(storedValue == null) {
                    return null;
                } else {
                    return adapter.adaptGet(storedValue);
                }
            }

            @Override
            public void optionalValue(T nullableValue) {
                if(nullableValue == null) {
                    setter.accept(null);
                } else {
                    setter.accept(adapter.adaptSet(nullableValue));
                }
            }
        };
    }
}
