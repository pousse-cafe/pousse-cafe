package poussecafe.attribute;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ListAttribute<T> extends Attribute<List<T>>, Iterable<T> {

    void add(T item);

    void filter(Predicate<T> predicate);

    Stream<T> stream();

    void addAll(Collection<T> values);
}
