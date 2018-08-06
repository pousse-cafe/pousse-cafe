package poussecafe.storable;

import java.util.Set;
import java.util.stream.Stream;

public interface SetProperty<T> extends Property<Set<T>> {

    void add(T item);

    boolean contains(T item);

    Stream<T> stream();

    int size();
}
