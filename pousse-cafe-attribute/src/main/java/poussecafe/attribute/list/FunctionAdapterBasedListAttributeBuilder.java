package poussecafe.attribute.list;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.list.ListAttributeBuilder.Complete;
import poussecafe.attribute.list.ListAttributeBuilder.ExpectingList;
import poussecafe.attribute.list.ListAttributeBuilder.ExpectingReadAdapters;
import poussecafe.attribute.list.ListAttributeBuilder.ExpectingWriteAdapters;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
class FunctionAdapterBasedListAttributeBuilder<U, T>
implements ExpectingReadAdapters<U, T>, ExpectingWriteAdapters<U, T>, ExpectingList<U, T>, Complete<T> {

    @Override
    public ExpectingWriteAdapters<U, T> adaptOnGet(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        this.readAdapter = adapter;
        return this;
    }

    private Function<U, T> readAdapter;

    @Override
    public ExpectingList<U, T> adaptOnSet(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        this.writeAdapter = adapter;
        return this;
    }

    private Function<T, U> writeAdapter;

    @Override
    public Complete<T> withList(List<U> list) {
        Objects.requireNonNull(list);
        this.list = list;
        return this;
    }

    private List<U> list;

    @Override
    public ListAttribute<T> build() {
        return new AdaptingListAttribute<>(list) {
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
