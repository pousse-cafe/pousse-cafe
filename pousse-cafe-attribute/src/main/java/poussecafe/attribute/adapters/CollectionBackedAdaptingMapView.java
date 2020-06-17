package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toList;

abstract class CollectionBackedAdaptingMapView<U, K, V, X>
implements Collection<X> {

    protected Collection<U> collection;

    protected Map<K, V> map;

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    protected DataAdapter<U, Entry<K, V>> adapter;

    @Override
    public Iterator<X> iterator() {
        return new AdaptingIterator.Builder<U, X>()
                .iterator(collection.iterator())
                .adapter(item -> applyProjection(adapter.adaptGet(item)))
                .onRemove(item -> map.remove(adapter.adaptGet(item).getKey()))
                .build();
    }

    protected abstract X applyProjection(Entry<K, V> item);

    @Override
    public Object[] toArray() {
        return Arrays.toAdaptedArray(collection, item -> applyProjection(adapter.adaptGet(item)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Arrays.toAdaptedArray(collection, item -> applyProjection(adapter.adaptGet(item)), a);
    }

    @Override
    public boolean add(X e) {
        throw new UnsupportedOperationException("add");
    }

    @Override
    public boolean addAll(Collection<? extends X> c) {
        throw new UnsupportedOperationException("addAll");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int previousSize = size();
        c.forEach(this::remove);
        return previousSize != size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        var iterator = iterator();
        var anyRemoved = false;
        while(iterator.hasNext()) {
            X next = iterator.next();
            if(!c.contains(next)) {
                iterator.remove();
                anyRemoved = true;
            }
        }
        return anyRemoved;
    }

    @Override
    public void clear() {
        collection.clear();
        map.clear();
    }

    void flushViewToCollection() {
        collection.clear();
        collection.addAll(map.entrySet().stream().map(adapter::adaptSet).collect(toList()));
    }
}
