package poussecafe.property;

import java.util.Map;

public class ReadWriteMapPropertyBuilder<K, V> {

    ReadWriteMapPropertyBuilder() {

    }

    public MapProperty<K, V> build(Map<K, V> map) {
        return new SimpleMapProperty<>(map);
    }
}
