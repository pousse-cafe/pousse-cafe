package poussecafe.attribute.set;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.set.SetAttributeBuilder.Complete;
import poussecafe.attribute.set.SetAttributeBuilder.ExpectingReadAdapters;
import poussecafe.attribute.set.SetAttributeBuilder.ExpectingSet;
import poussecafe.attribute.set.SetAttributeBuilder.ExpectingWriteAdapters;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
class FunctionAdapterBasedSetAttributeBuilder<U, T>
implements ExpectingReadAdapters<U, T>, ExpectingWriteAdapters<U, T>, ExpectingSet<U, T>, Complete<T> {

    @Override
    public ExpectingWriteAdapters<U, T> adaptOnGet(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        this.readAdapter = adapter;
        return this;
    }

    private Function<U, T> readAdapter;

    @Override
    public ExpectingSet<U, T> adaptOnSet(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        this.writeAdapter = adapter;
        return this;
    }

    private Function<T, U> writeAdapter;

    @Override
    public Complete<T> withSet(Set<U> set) {
        Objects.requireNonNull(set);
        this.set = set;
        return this;
    }

    private Set<U> set;

    @Override
    public SetAttribute<T> build() {
        return new ConvertingSetAttribute<>(set) {
            @Override
            protected T convertFrom(U from) {
                return readAdapter.apply(from);
            }

            @Override
            protected U convertTo(T from) {
                return writeAdapter.apply(from);
            }
        };
    }
}
