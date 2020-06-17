package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map.Entry;

class CollectionBackedAdaptingMapValues<U, K, V>
extends CollectionBackedAdaptingMapView<U, K, V, V> {

    CollectionBackedAdaptingMapValues() {

    }

    @Override
    public boolean contains(Object o) {
        return map.containsValue(o);
    }

    @Override
    protected V applyProjection(Entry<K, V> item) {
        return item.getValue();
    }

    @Override
    public boolean remove(Object o) {
        var iterator = iterator();
        var anyRemoved = false;
        while(iterator.hasNext()) {
            V next = iterator.next();
            if(o.equals(next)) {
                iterator.remove();
                anyRemoved = true;
            }
        }
        return anyRemoved;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.values().containsAll(c);
    }
}
