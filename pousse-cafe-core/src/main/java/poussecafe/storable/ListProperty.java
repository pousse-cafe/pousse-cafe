package poussecafe.storable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ListProperty<T> extends Property<List<T>> {

    void add(T item);

    void filter(Predicate<T> predicate);

    Stream<T> stream();
}
