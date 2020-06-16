package poussecafe.attribute.optional;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.optional.OptionalAttributeBuilder.Complete;
import poussecafe.attribute.optional.OptionalAttributeBuilder.ExpecingReadAdapter;
import poussecafe.attribute.optional.OptionalAttributeBuilder.ExpectingReader;
import poussecafe.attribute.optional.OptionalAttributeBuilder.ExpectingWriterAdapter;
import poussecafe.attribute.optional.OptionalAttributeBuilder.ExpectingWriterAfterAdapter;

import static java.util.Objects.requireNonNull;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
class FunctionAdapterBasedOptionalAttributeBuilder<U, T>
implements ExpecingReadAdapter<U, T>, ExpectingReader<U, T>, ExpectingWriterAdapter<U, T>,
ExpectingWriterAfterAdapter<U, T>, Complete<T> {

    FunctionAdapterBasedOptionalAttributeBuilder() {

    }

    @Override
    public ExpectingReader<U, T> adaptOnRead(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        this.readAdapter = adapter;
        return this;
    }

    Function<U, T> readAdapter;

    @Override
    public ExpectingWriterAdapter<U, T> read(Supplier<U> getter) {
        requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    Supplier<U> getter;

    @Override
    public ExpectingWriterAfterAdapter<U, T> adaptOnWrite(Function<T, U> adapter) {
        requireNonNull(adapter);
        writeAdapter = adapter;
        return this;
    }

    Function<T, U> writeAdapter;

    @Override
    public Complete<T> write(Consumer<U> setter) {
        requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    Consumer<U> setter;

    @Override
    public OptionalAttribute<T> build() {
        return new BaseOptionalAttribute<>() {
            @Override
            public T nullableValue() {
                U storedValue = getter.get();
                if(storedValue == null) {
                    return null;
                } else {
                    return readAdapter.apply(storedValue);
                }
            }

            @Override
            public void optionalValue(T nullableValue) {
                if(nullableValue == null) {
                    setter.accept(null);
                } else {
                    setter.accept(writeAdapter.apply(nullableValue));
                }
            }
        };
    }
}
