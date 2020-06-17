package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.function.Function;

public class Arrays {

    public static <U, T> Object[] toAdaptedArray(Collection<U> collection, Function<U, T> adapter) {
        var array = new Object[collection.size()];
        copyIntoArray(collection, adapter, array);
        return array;
    }

    private static <U, T> void copyIntoArray(Collection<U> collection, Function<U, T> adapter, Object[] destination) {
        var iterator = new AdaptingIterator.Builder<U, T>()
                .iterator(collection.iterator())
                .adapter(adapter)
                .build();
        for(int i = 0; i < collection.size(); ++i) {
            destination[i] = iterator.next();
        }
    }

    @SuppressWarnings("unchecked")
    public static <U, T> Object[] toAdaptedArray(Collection<U> collection, Function<U, T> adapter, T[] destination) {
        T[] array;
        if(destination.length < collection.size()) {
            array = (T[]) new Object[collection.size()];
        } else {
            array = destination;
        }
        copyIntoArray(collection, adapter, array);
        return array;
    }

    private Arrays() {

    }
}
