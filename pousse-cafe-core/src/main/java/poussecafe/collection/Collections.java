package poussecafe.collection;

import java.util.HashSet;
import java.util.Set;

public class Collections {

    @SafeVarargs
    public static <T> Set<T> asSet(T... items) {
        Set<T> result = new HashSet<>();
        for (T item : items) {
            result.add(item);
        }
        return result;
    }
}
