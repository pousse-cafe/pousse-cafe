package poussecafe.attribute;

import java.util.Set;
import java.util.stream.Stream;

public interface SetAttribute<T> extends Attribute<Set<T>> {

    boolean add(T item);

    boolean contains(T item);

    Stream<T> stream();

    int size();

    boolean remove(T item);
}
