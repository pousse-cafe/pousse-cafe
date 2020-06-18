package poussecafe.attribute.adapters;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.Objects.requireNonNull;

public class AdaptingMapEntry<L, U, K, V> implements Entry<K, V> {

    @Override
    public K getKey() {
        return keyAdapter.adaptGet(entry.getKey());
    }

    private Entry<L, U> entry;

    public Entry<L, U> underlyingEntry() {
        return entry;
    }

    private DataAdapter<L, K> keyAdapter;

    @Override
    public V getValue() {
        U value = entry.getValue();
        if(value == null) {
            return null;
        } else {
            return valueAdapter.adaptGet(value);
        }
    }

    private DataAdapter<U, V> valueAdapter;

    @Override
    public V setValue(V value) {
        U previousValue = entry.getValue();
        K key = keyAdapter.adaptGet(entry.getKey());
        mutableMap.put(key, value);
        return valueAdapter.adaptGet(previousValue);
    }

    private Map<K, V> mutableMap;

    public static class Builder<L, U, K, V> {

        private AdaptingMapEntry<L, U, K, V> entry = new AdaptingMapEntry<>();

        public AdaptingMapEntry<L, U, K, V> build() {
            requireNonNull(entry.entry);
            requireNonNull(entry.keyAdapter);
            requireNonNull(entry.valueAdapter);
            return entry;
        }

        public Builder<L, U, K, V> entry(Entry<L, U> entry) {
            this.entry.entry = entry;
            return this;
        }

        public Builder<L, U, K, V> keyAdapter(DataAdapter<L, K> keyAdapter) {
            entry.keyAdapter = keyAdapter;
            return this;
        }

        public Builder<L, U, K, V> valueAdapter(DataAdapter<U, V> valueAdapter) {
            entry.valueAdapter = valueAdapter;
            return this;
        }

        public Builder<L, U, K, V> mutableMap(Map<K, V> mutableMap) {
            entry.mutableMap = mutableMap;
            return this;
        }
    }

    private AdaptingMapEntry() {

    }
}