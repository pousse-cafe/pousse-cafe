package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import poussecafe.collection.MapEditor;

import static java.util.Objects.requireNonNull;

public class AdaptingMutableMap<L, U, K, V> implements EditableMap<K, V> {

    @Override
    public int size() {
        return mutableMap.size();
    }

    private Map<L, U> mutableMap;

    @Override
    public boolean isEmpty() {
        return mutableMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mutableMap.containsKey(adaptKToL(key));
    }

    @SuppressWarnings("unchecked")
    private L adaptKToL(Object key) {
        return keyAdapter.adaptSet((K) key);
    }

    private DataAdapter<L, K> keyAdapter;

    @Override
    public boolean containsValue(Object value) {
        return mutableMap.containsValue(adaptVToU(value));
    }

    @SuppressWarnings("unchecked")
    private U adaptVToU(Object value) {
        return valueAdapter.adaptSet((V) value);
    }

    private DataAdapter<U, V> valueAdapter;

    @Override
    public V get(Object key) {
        U value = mutableMap.get(adaptKToL(key));
        if(value == null) {
            return null;
        } else {
            return adaptUToV(value);
        }
    }

    @SuppressWarnings("unchecked")
    private V adaptUToV(Object value) {
        return valueAdapter.adaptGet((U) value);
    }

    @Override
    public V put(K key, V value) {
        U newValue = adaptVToU(value);
        U previousValue = mutableMap.put(adaptKToL(key), newValue);
        if(previousValue == null) {
            return null;
        } else {
            return adaptUToV(previousValue);
        }
    }

    @Override
    public V remove(Object key) {
        U removedValue = mutableMap.remove(adaptKToL(key));
        return adaptUToV(removedValue);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().stream().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear() {
        mutableMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return new AdaptingMutableSet.Builder<L, K>()
                .mutableSet(mutableMap.keySet())
                .adapter(keyAdapter)
                .build();
    }

    @Override
    public Collection<V> values() {
        return new AdaptingMutableMapValues.Builder<L, U, K, V>()
                .mutableMap(mutableMap)
                .keyAdapter(keyAdapter)
                .valueAdapter(valueAdapter)
                .build();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AdaptingMutableSet.Builder<Entry<L, U>, Entry<K, V>>()
                .mutableSet(mutableMap.entrySet())
                .adapter(DataAdapters.mutableEntry(keyAdapter, valueAdapter, this))
                .build();
    }

    @Override
    public MapEditor<K, V> edit() {
        return new MapEditor<>(this);
    }

    public static class Builder<L, U, K, V> {

        private AdaptingMutableMap<L, U, K, V> map = new AdaptingMutableMap<>();

        public AdaptingMutableMap<L, U, K, V> build() {
            requireNonNull(map.mutableMap);
            requireNonNull(map.keyAdapter);
            requireNonNull(map.valueAdapter);
            return map;
        }

        public Builder<L, U, K, V> mutableMap(Map<L, U> mutableMap) {
            map.mutableMap = mutableMap;
            return this;
        }

        public Builder<L, U, K, V> keyAdapter(DataAdapter<L, K> keyAdapter) {
            map.keyAdapter = keyAdapter;
            return this;
        }

        public Builder<L, U, K, V> valueAdapter(DataAdapter<U, V> valueAdapter) {
            map.valueAdapter = valueAdapter;
            return this;
        }
    }

    private AdaptingMutableMap() {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }
}
