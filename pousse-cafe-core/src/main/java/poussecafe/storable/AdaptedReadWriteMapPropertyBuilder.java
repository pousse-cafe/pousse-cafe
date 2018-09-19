package poussecafe.storable;

import java.util.Map;
import java.util.function.Function;
import poussecafe.storage.memory.ConvertingMapProperty;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class AdaptedReadWriteMapPropertyBuilder<J, U, K, V> {

    AdaptedReadWriteMapPropertyBuilder(
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

    public MapProperty<K, V> build(Map<J, U> map) {
        return new ConvertingMapProperty<J, U, K, V>(map) {
            @Override
            protected K convertFromKey(J from) {
                return readKeyAdapter.apply(from);
            }

            @Override
            protected V convertFromValue(U from) {
                return readValueAdapter.apply(from);
            }

            @Override
            protected J convertToKey(K from) {
                return writeKeyAdapter.apply(from);
            }

            @Override
            protected U convertToValue(V from) {
                return writeValueAdapter.apply(from);
            }
        };
    }
}
