package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <V> Attribute value type
 */
public class AdaptingMapAttributeBuilder<J, U, K, V> {

    public AdaptedReadOnlyMapAttributeBuilder<J, U, K, V> adaptOnGet(Function<J, K> idAdapter, Function<U, V> valueAdapter) {
        Objects.requireNonNull(idAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptedReadOnlyMapAttributeBuilder<>(idAdapter, valueAdapter);
    }
}
