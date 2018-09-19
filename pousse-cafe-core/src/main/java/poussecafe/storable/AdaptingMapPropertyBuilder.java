package poussecafe.storable;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class AdaptingMapPropertyBuilder<J, U, K, V> {

    public AdaptedMapPropertyBuilder<J, U, K, V> adapt(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        checkThatValue(keyAdapter).notNull();
        checkThatValue(valueAdapter).notNull();
        return new AdaptedMapPropertyBuilder<>(keyAdapter, valueAdapter);
    }
}
