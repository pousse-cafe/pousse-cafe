package poussecafe.attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import poussecafe.attribute.list.EditableList;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public interface ListAttribute<T> extends Attribute<List<T>>, Iterable<T> {

    @Override
    default List<T> value() {
        return Collections.unmodifiableList(mutableValue());
    }

    EditableList<T> mutableValue();

    @Override
    default void value(List<T> value) {
        requireNonNull(value);
        mutableValue().clear();
        mutableValue().addAll(value);
    }

    /**
     * @deprecated Use mutableValue().add(item)
     */
    @Deprecated(since = "0.19")
    default void add(T item) {
        mutableValue().add(item);
    }

    /**
     * @deprecated Use value(value().stream().filter(predicate).collect(toList()))
     */
    @Deprecated(since = "0.19")
    default void filter(Predicate<T> predicate) {
        value(value().stream().filter(predicate).collect(toList()));
    }

    /**
     * @deprecated value().stream()
     */
    @Deprecated(since = "0.19")
    default Stream<T> stream() {
        return value().stream();
    }

    /**
     * @deprecated Use mutableValue().addAll(values)
     */
    @Deprecated(since = "0.19")
    default void addAll(Collection<T> values) {
        mutableValue().addAll(values);
    }

    /**
     * @deprecated Use value().size()
     */
    @Deprecated(since = "0.19")
    default int size() {
        return value().size();
    }

    /**
     * @deprecated Use value().get(index)
     */
    @Deprecated(since = "0.19")
    default T get(int index) {
        return value().get(index);
    }

    /**
     * @deprecated Use mutableValue().clear()
     */
    @Deprecated(since = "0.19")
    default void clear() {
        mutableValue().clear();
    }

    /**
     * @deprecated Use mutableValue().set(index, item)
     */
    @Deprecated(since = "0.19")
    default void set(int index, T item) {
        mutableValue().set(index, item);
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
