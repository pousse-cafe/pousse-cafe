package poussecafe.storable;

import java.util.HashSet;
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
        set.clear();
        set.addAll(value);
    }

    @Override
    public void add(T item) {
        set.add(item);
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

}
