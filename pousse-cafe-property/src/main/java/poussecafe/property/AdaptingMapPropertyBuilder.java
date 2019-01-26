package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class AdaptingMapPropertyBuilder<J, U, K, V> {

    public AdaptedReadOnlyMapPropertyBuilder<J, U, K, V> adaptOnGet(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        return new AdaptedReadOnlyMapPropertyBuilder<>(keyAdapter, valueAdapter);
    }
}
