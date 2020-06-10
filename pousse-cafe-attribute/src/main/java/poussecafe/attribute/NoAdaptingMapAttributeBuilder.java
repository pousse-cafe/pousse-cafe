package poussecafe.attribute;

import java.util.Map;
import poussecafe.attribute.MapAttributeBuilder.Complete;

public class NoAdaptingMapAttributeBuilder<K, V>
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
