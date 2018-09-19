package poussecafe.storable;

import java.util.Map;

public class ReadOnlyMapPropertyBuilder<K, V> {

    ReadOnlyMapPropertyBuilder() {

    }

    public MapProperty<K, V> build(Map<K, V> map) {
        return new SimpleMapProperty<K, V>(map) {
            @Override
            public void set(Map<K, V> value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public V put(K key,
                    V value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public V remove(K key) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public ReadWriteMapPropertyBuilder<K, V> write() {
        return new ReadWriteMapPropertyBuilder<>();
    }
}
