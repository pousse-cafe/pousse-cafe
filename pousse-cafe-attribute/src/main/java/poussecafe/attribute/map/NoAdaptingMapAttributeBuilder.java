package poussecafe.attribute.map;

import java.util.Map;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.map.MapAttributeBuilder.Complete;

class NoAdaptingMapAttributeBuilder<K, V>
implements Complete<K, V> {

    NoAdaptingMapAttributeBuilder(Map<K, V> map) {
        this.map = map;
    }

    private Map<K, V> map;

    @Override
    public MapAttribute<K, V> build() {
        return new SimpleMapAttribute<>(map);
    }
}
