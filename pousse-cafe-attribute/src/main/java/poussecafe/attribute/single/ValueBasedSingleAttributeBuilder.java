package poussecafe.attribute.single;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.AutoAdapter;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.attribute.single.SingleAttributeBuilder.ExpectingAdaptedReader;
import poussecafe.attribute.single.SingleAttributeBuilder.ExpectingReadAdapter;
import poussecafe.attribute.single.SingleAttributeBuilder.ExpectingReaderOrAdapter;
import poussecafe.attribute.single.SingleAttributeBuilder.ExpectingWriter;

import static java.util.Objects.requireNonNull;

class ValueBasedSingleAttributeBuilder<T>
implements ExpectingReaderOrAdapter<T> {

    ValueBasedSingleAttributeBuilder(Class<T> valueClass) {
        requireNonNull(valueClass);
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    @Override
    public ExpectingWriter<T> read(Supplier<T> getter) {
        return new NoAdaptingSingleAttributeBuilder<>(getter);
    }

    @Override
    public <U extends AutoAdapter<T>> ExpectingAdaptedReader<U, T> usingAutoAdapter(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        return new DataAdapterBasedSingleAttributeBuilder<>(DataAdapters.auto(valueClass, autoAdapterClass));
    }

    @Override
    public <U> ExpectingReadAdapter<U, T> storedAs(Class<U> storedType) {
        requireNonNull(storedType);
        return new FunctionAdapterBasedSingleAttributeBuilder<>();
    }

    @Override
    public <U> ExpectingAdaptedReader<U, T> usingDataAdapter(DataAdapter<U, T> dataAdapter) {
        return new DataAdapterBasedSingleAttributeBuilder<>(dataAdapter);
    }
}
