package poussecafe.attribute;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <V> Attribute value type
 */
public class AdaptingReadWriteMapAttributeBuilder<J, U, K, V> {

    AdaptingReadWriteMapAttributeBuilder(
            Function<J, K> readKeyAdapter,
            Function<U, V> readValueAdapter,
            Function<K, J> writeKeyAdapter,
            Function<V, U> writeValueAdapter) {
        this.readKeyAdapter = readKeyAdapter;
        this.readValueAdapter = readValueAdapter;

        this.writeKeyAdapter = writeKeyAdapter;
        this.writeValueAdapter = writeValueAdapter;
    }

    private Function<J, K> readKeyAdapter;

    private Function<U, V> readValueAdapter;

    private Function<K, J> writeKeyAdapter;

    private Function<V, U> writeValueAdapter;

    public AdaptedReadWriteMapAttributeBuilder<J, U, K, V> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteMapAttributeBuilder<>(readKeyAdapter, readValueAdapter, writeKeyAdapter, writeValueAdapter, map);
    }
}
