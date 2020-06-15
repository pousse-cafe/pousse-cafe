package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.SingleAttributeBuilder.Complete;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingAdaptedReader;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingAdaptedWriter;
import poussecafe.attribute.adapters.DataAdapter;

class DataAdapterBasedSingleAttributeBuilder<U, T>
implements ExpectingAdaptedReader<U, T>, ExpectingAdaptedWriter<U, T>, Complete<T> {

    DataAdapterBasedSingleAttributeBuilder(DataAdapter<U, T> adapter) {
        Objects.requireNonNull(adapter);
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    @Override
    public ExpectingAdaptedWriter<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    private Supplier<U> getter;

    @Override
    public Complete<T> write(Consumer<U> setter) {
        Objects.requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<U> setter;

    @Override
    public Attribute<T> build() {
        return new Attribute<>() {
            @Override
            public T value() {
                U storedValue = getter.get();
                Objects.requireNonNull(storedValue, "Stored value cannot be null");
                return adapter.adaptGet(storedValue);
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value, "Stored value cannot be null");
                setter.accept(adapter.adaptSet(value));
            }
        };
    }
}
