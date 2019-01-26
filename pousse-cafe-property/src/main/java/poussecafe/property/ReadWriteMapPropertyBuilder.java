package poussecafe.property;

import java.util.Map;

public class ReadWriteMapPropertyBuilder<K, V> {

    ReadWriteMapPropertyBuilder(Map<K, V> map) {
        this.map = map;
    }

    private Map<K, V> map;

    public MapProperty<K, V> build() {
        return new SimpleMapProperty<>(map);
    }
}
