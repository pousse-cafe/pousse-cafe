package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <V> Attribute value type
 */
public class AdaptingMapAttributeBuilder<J, U, K, V> {

    public AdaptedReadOnlyMapAttributeBuilder<J, U, K, V> adaptOnGet(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptedReadOnlyMapAttributeBuilder<>(keyAdapter, valueAdapter);
    }
}
