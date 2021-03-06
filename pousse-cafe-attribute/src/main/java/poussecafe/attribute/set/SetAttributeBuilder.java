package poussecafe.attribute.set;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import poussecafe.attribute.AutoAdapter;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.adapters.AutoAdaptingDataAdapter;
import poussecafe.attribute.adapters.DataAdapter;

import static java.util.Objects.requireNonNull;

public class SetAttributeBuilder<T> {

    public SetAttributeBuilder(Class<T> elementClass) {
        requireNonNull(elementClass);
        this.elementClass = elementClass;
    }

    private Class<T> elementClass;

    public Complete<T> withSet(Set<T> set) {
        Objects.requireNonNull(set);
        return new NoAdaptingSetAttributeBuilder<>(set);
    }

    public static interface Complete<T> {

        SetAttribute<T> build();
    }

    public <U> ExpectingReadAdapters<U, T> itemsStoredAs(Class<U> storedElementType) {
        Objects.requireNonNull(storedElementType);
        return new FunctionAdapterBasedSetAttributeBuilder<>();
    }

    public static interface ExpectingReadAdapters<U, T> {

        ExpectingWriteAdapters<U, T> adaptOnGet(Function<U, T> adapter);
    }

    public static interface ExpectingWriteAdapters<U, T> {

        ExpectingSet<U, T> adaptOnSet(Function<T, U> adapter);
    }

    public static interface ExpectingSet<U, T> {

        Complete<T> withSet(Set<U> storageSet);

        Complete<T> withCollection(Collection<U> storageCollection);
    }

    public <U extends AutoAdapter<T>> ExpectingSet<U, T> usingItemAutoAdapter(Class<U> dataAdapterClass) {
        return usingItemDataAdapter(new AutoAdaptingDataAdapter<>(elementClass, dataAdapterClass));
    }

    public <U> ExpectingSet<U, T> usingItemDataAdapter(DataAdapter<U, T> dataAdapter) {
        return new DataAdapterBasedSetAttributeBuilder<>(dataAdapter);
    }
}
