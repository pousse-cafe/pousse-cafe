package poussecafe.check;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class Predicates {

    private Predicates() {

    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return value -> !predicate.test(value);
    }
    public static <K, V> Predicate<Map<K, V>> hasKey(K key) {
        return map -> map.containsKey(key);
    }

    public static <C extends Collection<T>, T> Predicate<C> hasItem(T item) {
        return collection -> collection.contains(item);
    }

    public static Predicate<String> emptyOrNullString() {
        return string -> string == null || string.isEmpty();
    }

    public static <T> Predicate<T> instanceOf(Class<?> objectClass) {
        return objectClass::isInstance;
    }

    public static <T> Predicate<T> equalTo(T other) {
        return value -> (value == null && other == null) || (value != null && value.equals(other));
    }

    public static Predicate<Integer> greaterThan(Integer other) {
        return integer -> integer != null && integer.compareTo(other) > 0;
    }

    public static Predicate<Integer> lessThan(Integer other) {
        return integer -> integer != null && integer.compareTo(other) < 0;
    }

    public static <C extends Collection<T>, T> Predicate<C> emptyOrNullCollection() {
        return collection -> collection == null || collection.isEmpty();
    }
}
