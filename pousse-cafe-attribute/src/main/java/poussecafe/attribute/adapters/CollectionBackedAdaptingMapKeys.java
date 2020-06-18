package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

class CollectionBackedAdaptingMapKeys<U, K, V>
extends CollectionBackedAdaptingMapView<U, K, V, K>
implements Set<K> {

    CollectionBackedAdaptingMapKeys() {

    }

    @Override
    public boolean contains(Object o) {
        return mapView.containsKey(o);
    }

    @Override
    protected K applyProjection(Entry<K, V> item) {
        return item.getKey();
    }

    @Override
    public boolean remove(Object o) {
        var oldValue = mapView.remove(o);
        if(oldValue != null) {
            flushViewToCollection();
        }
        return oldValue != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mapView.keySet().containsAll(c);
    }
}
