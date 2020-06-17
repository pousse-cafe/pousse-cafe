package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class AdaptingMutableMapValues<L, U, K, V>
implements Collection<V> {

    @Override
    public int size() {
        return mutableMap.size();
    }

    private Map<L, U> mutableMap;

    private DataAdapter<L, K> keyAdapter;

    private DataAdapter<U, V> valueAdapter;

    @Override
    public boolean isEmpty() {
        return mutableMap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        U value;
        try {
            value = valueAdapter.adaptSet((V) o);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableMap.containsValue(value);
    }

    @Override
    public Iterator<V> iterator() {
        return new AdaptingMutableMapValuesIterator.Builder<L, U, K, V>()
                .entrySetIterator(mutableMap.entrySet().iterator())
                .valueAdapter(valueAdapter)
                .build();
    }

    @Override
    public Object[] toArray() {
        return Arrays.toAdaptedArray(mutableMap.values(), valueAdapter::adaptGet);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Arrays.toAdaptedArray(mutableMap.values(), valueAdapter::adaptGet, a);
    }

    @Override
    public boolean add(V e) {
        throw new UnsupportedOperationException("add");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        U value;
        try {
            value = valueAdapter.adaptSet((V) o);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableMap.values().remove(value);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        throw new UnsupportedOperationException("addAll");
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
    public boolean retainAll(Collection<?> c) {
        boolean someRemoved = false;
        Iterator<V> iterator = iterator();
        while(iterator.hasNext()) {
            V next = iterator.next();
            if(!c.contains(next)) {
                iterator.remove();
                someRemoved = true;
            }
        }
        return someRemoved;
    }

    @Override
    public void clear() {
        mutableMap.clear();
    }

    public static class Builder<L, U, K, V> {

        private AdaptingMutableMapValues<L, U, K, V> values = new AdaptingMutableMapValues<>();

        public AdaptingMutableMapValues<L, U, K, V> build() {
            requireNonNull(values.mutableMap);
            requireNonNull(values.keyAdapter);
            requireNonNull(values.valueAdapter);
            return values;
        }

        public Builder<L, U, K, V> mutableMap(Map<L, U> mutableMap) {
            values.mutableMap = mutableMap;
            return this;
        }

        public Builder<L, U, K, V> keyAdapter(DataAdapter<L, K> keyAdapter) {
            values.keyAdapter = keyAdapter;
            return this;
        }

        public Builder<L, U, K, V> valueAdapter(DataAdapter<U, V> valueAdapter) {
            values.valueAdapter = valueAdapter;
            return this;
        }
    }
}
