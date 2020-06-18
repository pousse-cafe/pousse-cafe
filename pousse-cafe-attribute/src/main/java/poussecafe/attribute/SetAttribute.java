package poussecafe.attribute;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import poussecafe.attribute.set.EditableSet;

import static java.util.Objects.requireNonNull;

public interface SetAttribute<T> extends Attribute<Set<T>>, Iterable<T> {

    @Override
    EditableSet<T> value();

    @Override
    default void value(Set<T> value) {
        requireNonNull(value);
        value().clear();
        value().addAll(value);
    }

    /**
     * @deprecated Use value().add(item)
     */
    @Deprecated(since = "0.19")
    default boolean add(T item) {
        return value().add(item);
    }

    /**
     * @deprecated Use value().contains(item)
     */
    @Deprecated(since = "0.19")
    default boolean contains(T item) {
        return value().contains(item);
    }

    /**
     * @deprecated Use value().stream()
     */
    @Deprecated(since = "0.19")
    default Stream<T> stream() {
        return value().stream();
    }

    /**
     * @deprecated Use value().size()
     */
    @Deprecated(since = "0.19")
    default int size() {
        return value().size();
    }

    /**
     * @deprecated Use value().remove(item)
     */
    @Deprecated(since = "0.19")
    default boolean remove(T item) {
        return value().remove(item);
    }

    /**
     * @deprecated Use value().addAll(value)
     */
    @Deprecated(since = "0.19")
    default void addAll(Collection<T> value) {
        value().addAll(value);
    }

    /**
     * @deprecated Use value().clear()
     */
    @Deprecated(since = "0.19")
    default void clear() {
        value().clear();
    }

    /**
     * @deprecated Use value().isEmpty()
     */
    @Deprecated(since = "0.19")
    default boolean isEmpty() {
        return value().isEmpty();
    }

    /**
     * @deprecated Use value().iterator()
     */
    @Override
    @Deprecated(since = "0.19")
    default Iterator<T> iterator() {
        return value().iterator();
    }

    /**
     * @deprecated Use value().forEach()
     */
    @Override
    @Deprecated(since = "0.19")
    default void forEach(Consumer<? super T> action) {
        value().forEach(action);
    }

    /**
     * @deprecated Use value().spliterator()
     */
    @Override
    @Deprecated(since = "0.19")
    default Spliterator<T> spliterator() {
        return value().spliterator();
    }
}
