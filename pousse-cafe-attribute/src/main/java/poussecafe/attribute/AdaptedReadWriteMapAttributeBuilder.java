package poussecafe.attribute;

import java.util.Map;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <V> Attribute value type
 */
public class AdaptedReadWriteMapAttributeBuilder<J, U, K, V> {

    AdaptedReadWriteMapAttributeBuilder(
            Function<J, K> readKeyAdapter,
            Function<U, V> readValueAdapter,
            Function<K, J> writeKeyAdapter,
            Function<V, U> writeValueAdapter,
            Map<J, U> map) {
        this.readKeyAdapter = readKeyAdapter;
        this.readValueAdapter = readValueAdapter;

        this.writeKeyAdapter = writeKeyAdapter;
        this.writeValueAdapter = writeValueAdapter;

        this.map = map;
    }

    private Function<J, K> readKeyAdapter;

    private Function<U, V> readValueAdapter;

    private Function<K, J> writeKeyAdapter;

    private Function<V, U> writeValueAdapter;

    private Map<J, U> map;

    public MapAttribute<K, V> build() {
        return new ConvertingMapAttribute<J, U, K, V>(map) {
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
