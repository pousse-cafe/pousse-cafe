package poussecafe.property;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingOptionalPropertyBuilder<U, T> {

    AdaptingOptionalPropertyBuilder() {

    }

    public AdaptingReadOnlyOptionalPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadOnlyOptionalPropertyBuilder<>(adapter);
    }
}
