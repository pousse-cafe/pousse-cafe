package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import poussecafe.attribute.SingleAttributeBuilder.Complete;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingReadAdapter;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingReadBeforeWriteAdapter;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingWriteAdapter;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingWriteAfterAdapter;

import static java.util.Objects.requireNonNull;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class FunctionAdapterBasedSingleAttributeBuilder<U, T>
implements ExpectingReadAdapter<U, T>, ExpectingReadBeforeWriteAdapter<U, T>,ExpectingWriteAdapter<U, T>,
ExpectingWriteAfterAdapter<U, T>, Complete<T> {

    FunctionAdapterBasedSingleAttributeBuilder() {

    }

    @Override
    public ExpectingReadBeforeWriteAdapter<U, T> adaptOnRead(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        this.readAdapter = adapter;
        return this;
    }

    private Function<U, T> readAdapter;

    @Override
    public ExpectingWriteAdapter<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    private Supplier<U> getter;

    @Override
    public ExpectingWriteAfterAdapter<U, T> adaptOnWrite(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        writeAdapter = adapter;
        return this;
    }

    private Function<T, U> writeAdapter;

    @Override
    public Complete<T> write(Consumer<U> setter) {
        requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<U> setter;

    @Override
    public Attribute<T> build() {
        return new Attribute<>() {
            @Override
            public T value() {
                return readAdapter.apply(getter.get());
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                setter.accept(writeAdapter.apply(value));
            }
        };
    }
}
