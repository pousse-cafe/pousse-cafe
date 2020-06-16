package poussecafe.attribute.set;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import poussecafe.attribute.SetAttribute;

public class BaseSetAttribute<T> implements SetAttribute<T> {

    public BaseSetAttribute(Set<T> set) {
        Objects.requireNonNull(set);
        this.set = set;
    }

    private Set<T> set;

    @Override
    public Set<T> value() {
        return Collections.unmodifiableSet(set);
    }

    @Override
    public void value(Set<T> value) {
        Objects.requireNonNull(value);
        set.clear();
        set.addAll(value);
    }

    @Override
    public boolean add(T item) {
        return set.add(item);
    }

    @Override
    public boolean contains(T item) {
        return set.contains(item);
    }

    @Override
    public Stream<T> stream() {
        return set.stream();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean remove(T item) {
        return set.remove(item);
    }

    @Override
    public Iterator<T> iterator() {
        return value().iterator();
    }

    @Override
    public void addAll(Collection<T> value) {
        set.addAll(value);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }
}
