package poussecafe.property;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class AdaptingReadWriteMapPropertyBuilder<J, U, K, V> {

    AdaptingReadWriteMapPropertyBuilder(
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

    public AdaptedReadWriteMapPropertyBuilder<J, U, K, V> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteMapPropertyBuilder<>(readKeyAdapter, readValueAdapter, writeKeyAdapter, writeValueAdapter, map);
    }
}
