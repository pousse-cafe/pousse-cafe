package poussecafe.attribute.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import poussecafe.collection.SetEditor;

import static java.util.Objects.requireNonNull;

class SimpleEditableSet<T> implements EditableSet<T> {

    SimpleEditableSet(Set<T> list) {
        requireNonNull(list);
        this.set = list;
    }

    private Set<T> set;

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object key) {
        return set.contains(key);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <V> V[] toArray(V[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return set.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.contains(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return set.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Set) {
            return set.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public String toString() {
        return set.toString();
    }

    @Override
    public SetEditor<T> edit() {
        return new SetEditor<>(this);
    }
}
