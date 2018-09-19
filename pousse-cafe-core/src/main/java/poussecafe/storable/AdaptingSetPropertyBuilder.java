package poussecafe.storable;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingSetPropertyBuilder<U, T> {

    public AdaptedSetPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptedSetPropertyBuilder<>(adapter);
    }
}
