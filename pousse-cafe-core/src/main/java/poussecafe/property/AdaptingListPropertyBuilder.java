package poussecafe.property;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingListPropertyBuilder<U, T> {

    public AdaptedListPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptedListPropertyBuilder<>(adapter);
    }
}
