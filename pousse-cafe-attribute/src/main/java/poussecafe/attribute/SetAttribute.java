package poussecafe.attribute;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public interface SetAttribute<T> extends Attribute<Set<T>>, Iterable<T> {

    boolean add(T item);

    boolean contains(T item);

    Stream<T> stream();

    int size();

    boolean remove(T item);

    void addAll(Collection<T> value);

    void clear();
}
