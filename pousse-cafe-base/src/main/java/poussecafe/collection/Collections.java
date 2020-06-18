package poussecafe.collection;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collections {

    @SafeVarargs
    public static <T> Set<T> asSet(T... items) {
        Set<T> result = new HashSet<>();
        java.util.Collections.addAll(result, items);
        return result;
    }

    public static <V> ListEditor<V> edit(List<V> list) {
        return new ListEditor<>(list);
    }

    public static <K, V> MapEditor<K, V> edit(Map<K, V> map) {
        return new MapEditor<>(map);
    }

    public static <V> SetEditor<V> edit(Set<V> set) {
        return new SetEditor<>(set);
    }

    private Collections() {

    }
}
