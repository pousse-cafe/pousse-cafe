package poussecafe.property;

import java.util.Set;
import java.util.stream.Stream;

public interface SetProperty<T> extends Property<Set<T>> {

    boolean add(T item);

    boolean contains(T item);

    Stream<T> stream();

    int size();

    boolean remove(T item);
}
