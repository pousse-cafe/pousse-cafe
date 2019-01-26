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
public class AdaptedReadOnlyMapPropertyBuilder<J, U, K, V> {

    AdaptedReadOnlyMapPropertyBuilder(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        this.keyAdapter = keyAdapter;
        this.valueAdapter = valueAdapter;
    }

    private Function<J, K> keyAdapter;

    private Function<U, V> valueAdapter;

    public MapProperty<K, V> build(Map<J, U> map) {
        return new ConvertingMapProperty<J, U, K, V>(map) {

            @Override
            protected K convertFromKey(J from) {
                return keyAdapter.apply(from);
            }

            @Override
            protected V convertFromValue(U from) {
                return valueAdapter.apply(from);
            }

            @Override
            protected J convertToKey(K from) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected U convertToValue(V from) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public AdaptingReadWriteMapPropertyBuilder<J, U, K, V> adapt(Function<K, J> keyAdapter, Function<V, U> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptingReadWriteMapPropertyBuilder<>(this.keyAdapter, this.valueAdapter, keyAdapter, valueAdapter);
    }
}
