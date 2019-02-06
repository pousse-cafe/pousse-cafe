package poussecafe.attribute;

import java.util.Map;

public class ReadWriteMapAttributeBuilder<K, V> {

    ReadWriteMapAttributeBuilder(Map<K, V> map) {
        this.map = map;
    }

    private Map<K, V> map;

    public MapAttribute<K, V> build() {
        return new SimpleMapAttribute<>(map);
    }
}
