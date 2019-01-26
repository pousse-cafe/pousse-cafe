package poussecafe.property;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class BaseSetProperty<T> implements SetProperty<T> {

    public BaseSetProperty(Set<T> set) {
        checkThat(value(set).notNull());
        this.set = set;
    }

    private Set<T> set;

    @Override
    public Set<T> get() {
        return new HashSet<>(set);
    }

    @Override
    public void set(Set<T> value) {
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

}
