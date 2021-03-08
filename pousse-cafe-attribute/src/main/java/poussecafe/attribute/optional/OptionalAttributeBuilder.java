package poussecafe.attribute.optional;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import poussecafe.attribute.AutoAdapter;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;

import static java.util.Objects.requireNonNull;

public class OptionalAttributeBuilder<T> {

    public OptionalAttributeBuilder(Class<T> valueClass) {
        requireNonNull(valueClass);
        propertyTypeClass = valueClass;
    }

    private Class<T> propertyTypeClass;

    public ExpecingWriter<T> read(Supplier<T> getter) {
        return new NoAdaptingOptionalAttributeBuilder<>(getter);
    }

    public static interface ExpecingWriter<T> {

        Complete<T> write(Consumer<T> setter);
    }

    public static interface Complete<T> {

        OptionalAttribute<T> build();
    }

    public <U> ExpecingReadAdapter<U, T> storedAs(Class<U> storedType) {
        requireNonNull(storedType);
        return new FunctionAdapterBasedOptionalAttributeBuilder<>();
    }

    public static interface ExpecingReadAdapter<U, T> {

        ExpectingReader<U, T> adaptOnRead(Function<U, T> adapter);
    }

    public static interface ExpectingReader<U, T> {

        ExpectingWriterAdapter<U, T> read(Supplier<U> getter);
    }

    public static interface ExpectingWriterAdapter<U, T> {

        ExpectingWriterAfterAdapter<U, T> adaptOnWrite(Function<T, U> adapter);
    }

    public static interface ExpectingWriterAfterAdapter<U, T> {

        Complete<T> write(Consumer<U> setter);
    }

    public <U> ExpectingAdaptedReader<U, T> usingDataAdapter(DataAdapter<U, T> adapter) {
        return new DataAdapterBasedOptionalAttributeBuilder<>(adapter);
    }

    public static interface ExpectingAdaptedReader<U, T> {

        ExpectingWriterAfterAdapter<U, T> read(Supplier<U> getter);
    }

    public <U extends AutoAdapter<T>> ExpectingAdaptedReader<U, T> usingAutoAdapter(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        return usingDataAdapter(DataAdapters.auto(propertyTypeClass, autoAdapterClass));
    }
}
