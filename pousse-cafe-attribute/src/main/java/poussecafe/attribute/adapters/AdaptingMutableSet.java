package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class AdaptingMutableSet<U, T> implements Set<T> {

    @Override
    public int size() {
        return mutableSet.size();
    }

    private Set<U> mutableSet;

    @Override
    public boolean isEmpty() {
        return mutableSet.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        U element;
        try {
            element = adapter.adaptSet((T) o);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableSet.contains(element);
    }

    private DataAdapter<U, T> adapter;

    @Override
    public Iterator<T> iterator() {
        return new AdaptingIterator.Builder<U, T>()
                .iterator(mutableSet.iterator())
                .adapter(adapter::adaptGet)
                .build();
    }

    @Override
    public Object[] toArray() {
        return Arrays.toAdaptedArray(mutableSet, adapter::adaptGet);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V[] toArray(V[] a) {
        return (V[]) Arrays.toAdaptedArray(mutableSet, adapter::adaptGet, a);
    }

    @Override
    public boolean add(T e) {
        U newElement = adapter.adaptSet(e);
        return mutableSet.add(newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        U elementToRemove;
        try {
            elementToRemove = adapter.adaptSet((T) o);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableSet.add(elementToRemove);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object element : c) {
            if(!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean someAdded = false;
        for(T element : c) {
            if(add(element)) {
                someAdded = true;
            }
        }
        return someAdded;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean someRemoved = false;
        Iterator<T> iterator = iterator();
        while(iterator.hasNext()) {
            T next = iterator.next();
            if(!c.contains(next)) {
                iterator.remove();
                someRemoved = true;
            }
        }
        return someRemoved;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean someRemoved = false;
        for(Object element : c) {
            if(remove(element)) {
                someRemoved = true;
            }
        }
        return someRemoved;
    }

    @Override
    public void clear() {
        mutableSet.clear();
    }

    public static class Builder<U, T> {

        private AdaptingMutableSet<U, T> set = new AdaptingMutableSet<>();

        public AdaptingMutableSet<U, T> build() {
            requireNonNull(set.mutableSet);
            requireNonNull(set.adapter);
            return set;
        }

        public Builder<U, T> mutableSet(Set<U> mutableSet) {
            set.mutableSet = mutableSet;
            return this;
        }

        public Builder<U, T> adapter(DataAdapter<U, T> adapter) {
            set.adapter = adapter;
            return this;
        }
    }

    private AdaptingMutableSet() {

    }
}
