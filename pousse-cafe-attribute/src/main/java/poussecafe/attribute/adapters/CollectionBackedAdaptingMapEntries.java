package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class CollectionBackedAdaptingMapEntries<U, K, V>
extends CollectionBackedAdaptingMapView<U, K, V, Entry<K, V>>
implements Set<Entry<K, V>> {

    CollectionBackedAdaptingMapEntries() {

    }

    @Override
    public boolean contains(Object o) {
        return mapView.entrySet().contains(o);
    }

    @Override
    protected Entry<K, V> applyProjection(Entry<K, V> item) {
        return wrapWithMutableEntry(item);
    }

    private Entry<K, V> wrapWithMutableEntry(Entry<K, V> item) {
        return DataAdapters.<K, V, K, V>mutableEntry(
                DataAdapters.adapter(key -> key, key -> key),
                DataAdapters.adapter(value -> value, value -> value),
                mutableMap).adaptGet(item);
    }

    Map<K, V> mutableMap;

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        Entry<K, V> entry;
        try {
            entry = (Entry<K, V>) o;
        } catch (ClassCastException e) {
            return false;
        }
        var entryRemoved = mapView.remove(entry.getKey(), entry.getValue());
        if(entryRemoved) {
            flushViewToCollection();
        }
        return entryRemoved;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mapView.entrySet().containsAll(c);
    }
}
