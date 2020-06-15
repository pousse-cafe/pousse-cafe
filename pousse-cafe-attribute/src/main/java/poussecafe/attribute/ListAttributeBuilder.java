package poussecafe.attribute;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.adapters.DataAdapter;

import static java.util.Objects.requireNonNull;

public class ListAttributeBuilder<T> {

    ListAttributeBuilder(Class<T> elementClass) {
        requireNonNull(elementClass);
        this.elementClass = elementClass;
    }

    private Class<T> elementClass;

    public Complete<T> withList(List<T> list) {
        Objects.requireNonNull(list);
        return new NoAdaptingListAttributeBuilder<>(list);
    }

    public static interface Complete<T> {

        ListAttribute<T> build();
    }

    public <U> ExpectingReadAdapters<U, T> itemsStoredAs(Class<U> storedElementType) {
        requireNonNull(storedElementType);
        return new FunctionAdapterBasedListAttributeBuilder<>();
    }

    public static interface ExpectingReadAdapters<U, T> {

        ExpectingWriteAdapters<U, T> adaptOnGet(Function<U, T> adapter);
    }

    public static interface ExpectingWriteAdapters<U, T> {

        ExpectingList<U, T> adaptOnSet(Function<T, U> adapter);
    }

    public static interface ExpectingList<U, T> {

        Complete<T> withList(List<U> storageList);
    }

    public <U> ExpectingList<U, T> usingItemAutoAdapter(Class<U> dataAdapterClass) {
        return usingItemDataAdapter(new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass));
    }

    public <U> ExpectingList<U, T> usingItemDataAdapter(DataAdapter<U, T> dataAdapter) {
        Objects.requireNonNull(dataAdapter);
        DataAdapterBasedListAttributeBuilder<U, T> builder = new DataAdapterBasedListAttributeBuilder<>();
        builder.adapter = dataAdapter;
        return builder;
    }
}
