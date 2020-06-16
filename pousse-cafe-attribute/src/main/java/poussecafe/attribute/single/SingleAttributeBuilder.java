package poussecafe.attribute.single;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.adapters.DataAdapter;

public class SingleAttributeBuilder<T> {

    public ExpectingReaderOrAdapter<T> usingValue(Class<T> valueClass) {
        return new ValueBasedSingleAttributeBuilder<>(valueClass);
    }

    public static interface ExpectingReaderOrAdapter<T> {

        ExpectingWriter<T> read(Supplier<T> writer);

        <U> ExpectingAdaptedReader<U, T> usingAutoAdapter(Class<U> autoAdapterClass);

        <U> ExpectingReadAdapter<U, T> storedAs(Class<U> storedType);

        <U> ExpectingAdaptedReader<U, T> usingDataAdapter(DataAdapter<U, T> dataAdapter);
    }

    public static interface ExpectingAdaptedReader<U, T> {

        ExpectingAdaptedWriter<U, T> read(Supplier<U> getter);
    }

    public static interface ExpectingAdaptedWriter<U, T> {

        Complete<T> write(Consumer<U> setter);
    }

    public static interface Complete<T> {

        Attribute<T> build();
    }

    public static interface ExpectingWriter<T> {

        Complete<T> write(Consumer<T> writer);
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
}
