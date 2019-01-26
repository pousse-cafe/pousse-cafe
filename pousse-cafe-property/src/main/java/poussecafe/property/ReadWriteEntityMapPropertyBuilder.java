package poussecafe.property;

import java.util.Map;

public class ReadWriteEntityMapPropertyBuilder<K, V> {

    ReadWriteEntityMapPropertyBuilder() {

    }

    public MapProperty<K, V> build(Map<K, V> map) {
        return new SimpleMapProperty<>(map);
    }
}
