package poussecafe.property;

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

    public AdaptingReadWriteMapPropertyBuilder<J, U, K, V> adaptOnSet(Function<K, J> keyAdapter, Function<V, U> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptingReadWriteMapPropertyBuilder<>(this.keyAdapter, this.valueAdapter, keyAdapter, valueAdapter);
    }
}
