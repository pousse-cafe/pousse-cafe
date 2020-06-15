package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;

import static java.util.Objects.requireNonNull;

public class SingleAttributeBuilder<T> {

    SingleAttributeBuilder(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    public ExpectingWriter<T> read(Supplier<T> getter) {
        return new NoAdaptingSingleAttributeBuilder<>(getter);
    }

    public static interface ExpectingWriter<T> {

        Complete<T> write(Consumer<T> writer);
    }

    public static interface Complete<T> {

        Attribute<T> build();
    }

    public <U> ExpectingReadAdapter<U, T> storedAs(Class<U> storedType) {
        requireNonNull(storedType);
        return new FunctionAdapterBasedSingleAttributeBuilder<>();
    }

    public static interface ExpectingReadAdapter<U, T> {

        ExpectingReadBeforeWriteAdapter<U, T> adaptOnRead(Function<U, T> adapter);
    }

    public static interface ExpectingReadBeforeWriteAdapter<U, T> {

        ExpectingWriteAdapter<U, T> read(Supplier<U> getter);
    }

    public static interface ExpectingWriteAdapter<U, T> {

        ExpectingWriteAfterAdapter<U, T> adaptOnWrite(Function<T, U> adapter);
    }

    public static interface ExpectingWriteAfterAdapter<U, T> {

        Complete<T> write(Consumer<U> writer);
    }

    public <U> ExpectingAdaptedReader<U, T> usingDataAdapter(DataAdapter<U, T> dataAdapter) {
        return new DataAdapterBasedSingleAttributeBuilder<>(dataAdapter);
    }

    public static interface ExpectingAdaptedReader<U, T> {

        ExpectingAdaptedWriter<U, T> read(Supplier<U> getter);
    }

    public static interface ExpectingAdaptedWriter<U, T> {

        Complete<T> write(Consumer<U> setter);
    }

    public <U> ExpectingAdaptedReader<U, T> usingAutoAdapter(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        return usingDataAdapter(DataAdapters.auto(valueClass, autoAdapterClass));
    }
}
