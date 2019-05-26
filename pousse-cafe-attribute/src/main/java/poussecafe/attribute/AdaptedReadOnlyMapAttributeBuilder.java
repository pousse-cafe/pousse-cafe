package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <V> Attribute value type
 */
public class AdaptedReadOnlyMapAttributeBuilder<J, U, K, V> {

    AdaptedReadOnlyMapAttributeBuilder(Function<J, K> idAdapter, Function<U, V> valueAdapter) {
        this.idAdapter = idAdapter;
        this.valueAdapter = valueAdapter;
    }

    private Function<J, K> idAdapter;

    private Function<U, V> valueAdapter;

    public AdaptingReadWriteMapAttributeBuilder<J, U, K, V> adaptOnWrite(Function<K, J> idAdapter, Function<V, U> valueAdapter) {
        Objects.requireNonNull(idAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptingReadWriteMapAttributeBuilder<>(this.idAdapter, this.valueAdapter, idAdapter, valueAdapter);
    }
}
