package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import poussecafe.attribute.map.EditableMap;
import poussecafe.collection.MapEditor;

import static java.util.Objects.requireNonNull;
import static poussecafe.attribute.adapters.DataAdapters.nullOrAdapted;

public class AdaptingMap<L, U, K, V> implements EditableMap<K, V> {

    @Override
    public int size() {
        return mutableMap.size();
    }

    private Map<L, U> mutableMap;

    @Override
    public boolean isEmpty() {
        return mutableMap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        L adaptedKey;
        try {
            adaptedKey = adaptKToL((K) key);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableMap.containsKey(adaptedKey);
    }

    @SuppressWarnings("unchecked")
    private L adaptKToL(K key) {
        return nullOrAdapted(key, keyAdapter::adaptSet);
    }

    private DataAdapter<L, K> keyAdapter;

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        U adaptedValue;
        try {
            adaptedValue = adaptVToU((V) value);
        } catch (ClassCastException e) {
            return false;
        }
        return mutableMap.containsValue(adaptedValue);
    }

    private U adaptVToU(V value) {
        return nullOrAdapted(value, valueAdapter::adaptSet);
    }

    private DataAdapter<U, V> valueAdapter;

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        L adaptedKey;
        try {
            adaptedKey = adaptKToL((K) key);
        } catch (ClassCastException e) {
            return null;
        }
        U value = mutableMap.get(adaptedKey);
        if(value == null) {
            return null;
        } else {
            return adaptUToV(value);
        }
    }

    private V adaptUToV(U value) {
        return nullOrAdapted(value, valueAdapter::adaptGet);
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

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        L adaptedKey;
        try {
            adaptedKey = adaptKToL((K) key);
        } catch (ClassCastException e) {
            return null;
        }
        U removedValue = mutableMap.remove(adaptedKey);
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
        return new AdaptingSet.Builder<L, K>()
                .mutableSet(mutableMap.keySet())
                .adapter(keyAdapter)
                .build();
    }

    @Override
    public Collection<V> values() {
        return new AdaptingMapValues.Builder<L, U, K, V>()
                .mutableMap(mutableMap)
                .keyAdapter(keyAdapter)
                .valueAdapter(valueAdapter)
                .build();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AdaptingSet.Builder<Entry<L, U>, Entry<K, V>>()
                .mutableSet(mutableMap.entrySet())
                .adapter(DataAdapters.mutableEntry(keyAdapter, valueAdapter, this))
                .build();
    }

    @Override
    public MapEditor<K, V> edit() {
        return new MapEditor<>(this);
    }

    public static class Builder<L, U, K, V> {

        private AdaptingMap<L, U, K, V> map = new AdaptingMap<>();

        public AdaptingMap<L, U, K, V> build() {
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

    private AdaptingMap() {

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
