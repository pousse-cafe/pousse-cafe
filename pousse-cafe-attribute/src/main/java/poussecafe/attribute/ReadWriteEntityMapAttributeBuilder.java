package poussecafe.attribute;

import java.util.Map;

public class ReadWriteEntityMapAttributeBuilder<K, V> {

    ReadWriteEntityMapAttributeBuilder() {

    }

    public MapAttribute<K, V> build(Map<K, V> map) {
        return new SimpleMapAttribute<>(map);
    }
}
