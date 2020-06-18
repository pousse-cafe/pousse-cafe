package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public abstract class AdaptingCollection<U, T> implements Collection<T> {

    @Override
    public int size() {
        return mutableCollection.size();
    }

    private Collection<U> mutableCollection;

    protected void mutableCollection(Collection<U> mutableCollection) {
        requireNonNull(mutableCollection);
        this.mutableCollection = mutableCollection;
    }

    protected Collection<U> mutableCollection() {
        return mutableCollection;
    }

    @Override
    public boolean isEmpty() {
        return mutableCollection.isEmpty();
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
        return mutableCollection.contains(element);
    }

    private DataAdapter<U, T> adapter;

    protected void adapter(DataAdapter<U, T> adapter) {
        requireNonNull(adapter);
        this.adapter = adapter;
    }

    protected DataAdapter<U, T> adapter() {
        return adapter;
    }

    @Override
    public Iterator<T> iterator() {
        return new AdaptingIterator.Builder<U, T>()
                .iterator(mutableCollection.iterator())
                .adapter(adapter::adaptGet)
                .build();
    }

    @Override
    public Object[] toArray() {
        return Arrays.toAdaptedArray(mutableCollection, adapter::adaptGet);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V[] toArray(V[] a) {
        return (V[]) Arrays.toAdaptedArray(mutableCollection, adapter::adaptGet, a);
    }

    @Override
    public boolean add(T e) {
        U newElement = adapter.adaptSet(e);
        return mutableCollection.add(newElement);
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
        return mutableCollection.remove(elementToRemove);
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
        mutableCollection.clear();
    }

    protected AdaptingCollection() {

    }
}
