package poussecafe.property;

public class MapPropertyBuilder<K, V> {

    MapPropertyBuilder() {

    }

    public ReadOnlyMapPropertyBuilder<K, V> read() {
        return new ReadOnlyMapPropertyBuilder<>();
    }

    public <J, U> AdaptingMapPropertyBuilder<J, U, K, V> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingMapPropertyBuilder<>();
    }
}
