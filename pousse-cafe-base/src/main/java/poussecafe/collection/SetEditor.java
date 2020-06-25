package poussecafe.collection;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class SetEditor<T> {

    public SetEditor() {
        this(new HashSet<>());
    }

    public SetEditor(Set<T> set) {
        requireNonNull(set);
        this.set = set;
    }

    private Set<T> set;

    public SetEditor<T> add(T item) {
        set.add(item);
        return this;
    }

    public SetEditor<T> remove(T item) {
        set.remove(item);
        return this;
    }

    public Set<T> finish() {
        return set;
    }
}