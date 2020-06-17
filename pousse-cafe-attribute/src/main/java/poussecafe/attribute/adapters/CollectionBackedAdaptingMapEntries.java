package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

class CollectionBackedAdaptingMapEntries<U, K, V>
extends CollectionBackedAdaptingMapView<U, K, V, Entry<K, V>>
implements Set<Entry<K, V>> {

    CollectionBackedAdaptingMapEntries() {

    }

    @Override
    public boolean contains(Object o) {
        return map.entrySet().contains(o);
    }

    @Override
    protected Entry<K, V> applyProjection(Entry<K, V> item) {
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        Entry<K, V> entry;
        try {
            entry = (Entry<K, V>) o;
        } catch (ClassCastException e) {
            return false;
        }
        var entryRemoved = map.remove(entry.getKey(), entry.getValue());
        if(entryRemoved) {
            flushViewToCollection();
        }
        return entryRemoved;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.entrySet().containsAll(c);
    }
}
