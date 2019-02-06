package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <V> Attribute value type
 */
public class AdaptedReadOnlyMapAttributeBuilder<J, U, K, V> {

    AdaptedReadOnlyMapAttributeBuilder(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        this.keyAdapter = keyAdapter;
        this.valueAdapter = valueAdapter;
    }

    private Function<J, K> keyAdapter;

    private Function<U, V> valueAdapter;

    public AdaptingReadWriteMapAttributeBuilder<J, U, K, V> adaptOnSet(Function<K, J> keyAdapter, Function<V, U> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptingReadWriteMapAttributeBuilder<>(this.keyAdapter, this.valueAdapter, keyAdapter, valueAdapter);
    }
}
